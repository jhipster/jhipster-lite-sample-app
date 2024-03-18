package tech.jhipster.lite.sample.wire.liquibase.infrastructure.secondary;

import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.Executor;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import tech.jhipster.lite.sample.Logs;
import tech.jhipster.lite.sample.LogsSpy;
import tech.jhipster.lite.sample.LogsSpyExtension;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
@ExtendWith(LogsSpyExtension.class)
class AsyncSpringLiquibaseTest {

  private final ConfigurableEnvironment environment = new MockEnvironment();
  private final Executor executor = spy(new DirectExecutor());
  private final LiquibaseProperties liquibaseProperties = new LiquibaseProperties();

  @Logs
  private LogsSpy logs;

  @Nested
  class AfterPropertiesSetTest {

    private final AsyncSpringLiquibase asyncSpringLiquibase = spy(new AsyncSpringLiquibase(executor, environment, liquibaseProperties));

    @Test
    void shouldDoNothingIfLiquibaseIsDisabled() throws LiquibaseException {
      liquibaseProperties.setEnabled(false);

      asyncSpringLiquibase.afterPropertiesSet();

      verify(asyncSpringLiquibase, never()).initDb();
      logs.shouldHave(Level.DEBUG, "Liquibase is disabled");
    }

    @Test
    void shouldStartSynchronouslyWhenActiveProfileIsNotLocal() throws LiquibaseException {
      liquibaseProperties.setEnabled(true);
      environment.setActiveProfiles("prod");
      doNothing().when(asyncSpringLiquibase).initDb();

      asyncSpringLiquibase.afterPropertiesSet();

      verify(executor, never()).execute(any());
      logs.shouldHave(Level.DEBUG, "Starting Liquibase synchronously");
    }

    @Test
    void shouldStartAsynchronouslyWhenActiveProfileIsLocal() throws LiquibaseException {
      liquibaseProperties.setEnabled(true);
      environment.setActiveProfiles("local");
      doNothing().when(asyncSpringLiquibase).initDb();
      when(asyncSpringLiquibase.getDataSource()).thenReturn(mock(DataSource.class));

      asyncSpringLiquibase.afterPropertiesSet();

      verify(executor).execute(any());
      logs.shouldHave(Level.WARN, "Starting Liquibase asynchronously, your database might not be ready at startup!");
    }

    @Test
    void shouldLogErrorOnLiquibaseException() throws LiquibaseException {
      liquibaseProperties.setEnabled(true);
      environment.setActiveProfiles("local");
      when(asyncSpringLiquibase.getDataSource()).thenReturn(mock(DataSource.class));
      doThrow(new LiquibaseException("some liquibase message")).when(asyncSpringLiquibase).initDb();

      asyncSpringLiquibase.afterPropertiesSet();

      logs.shouldHave(Level.ERROR, "Liquibase could not start correctly, your database is NOT ready: some liquibase message");
    }

    @Test
    void shouldLogErrorOnSQLException() throws LiquibaseException, SQLException {
      liquibaseProperties.setEnabled(true);
      environment.setActiveProfiles("local");
      DataSource mockedDataSource = mock(DataSource.class);
      when(asyncSpringLiquibase.getDataSource()).thenReturn(mockedDataSource);
      doThrow(new SQLException("some sql message")).when(mockedDataSource).getConnection();

      asyncSpringLiquibase.afterPropertiesSet();

      logs.shouldHave(Level.ERROR, "Liquibase could not start correctly, your database is NOT ready: some sql message");
    }
  }

  @Nested
  class InitDbTest {

    @Test
    void shouldWarnWhenLiquibaseExecutionIsSlow() throws LiquibaseException {
      liquibaseProperties.setEnabled(true);
      Duration slownessThreshold = Duration.ofMillis(50);
      TestAsyncSpringLiquibase asyncSpringLiquibase = spy(
        new TestAsyncSpringLiquibase(executor, environment, liquibaseProperties, slownessThreshold)
      );
      asyncSpringLiquibase.setFakeDuration(slownessThreshold.plusMillis(1));

      asyncSpringLiquibase.initDb();

      logs.shouldHave(Level.DEBUG, "Liquibase has updated your database in");
      logs.shouldHave(Level.WARN, "Warning, Liquibase took more than %s seconds to start up!".formatted(slownessThreshold.toSeconds()));
    }
  }

  private static final class DirectExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
      command.run();
    }
  }

  private static class TestAsyncSpringLiquibase extends AsyncSpringLiquibase {

    private Duration fakeDuration = Duration.ZERO;

    public TestAsyncSpringLiquibase(
      Executor executor,
      Environment environment,
      LiquibaseProperties liquibaseProperties,
      Duration slownessThreshold
    ) {
      super(executor, environment, liquibaseProperties, slownessThreshold);
    }

    public void setFakeDuration(Duration fakeDuration) {
      this.fakeDuration = fakeDuration;
    }

    @Override
    public DataSource getDataSource() {
      DataSource source = mock(DataSource.class);
      try {
        doReturn(mock(Connection.class)).when(source).getConnection();
      } catch (SQLException exception) {
        // This should never happen
        throw new AssertionError(exception);
      }
      return source;
    }

    @Override
    protected Liquibase createLiquibase(Connection c) {
      return null;
    }

    @Override
    @SuppressWarnings("java:S2925")
    protected void performUpdate(Liquibase liquibase) {
      try {
        Thread.sleep(fakeDuration.toMillis());
      } catch (InterruptedException exception) {
        // This should never happen
        throw new Error(exception);
      }
    }
  }
}
