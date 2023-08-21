package tech.jhipster.lite.sample.wire.liquibase.infrastructure.secondary;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.Executor;
import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.util.StopWatch;

/**
 * Specific liquibase.integration.spring.SpringLiquibase that will update the database asynchronously and close
 * DataSource if necessary. <p> By default, this asynchronous version only works when using the "dev" profile.<p> The standard
 * liquibase.integration.spring.SpringLiquibase starts Liquibase in the current thread: <ul> <li>This is needed if you
 * want to do some database requests at startup</li> <li>This ensure that the database is ready when the application
 * starts</li> </ul> But as this is a rather slow process, we use this asynchronous version to speed up our start-up
 * time: <ul> <li>On a recent MacBook Pro, start-up time is down from 14 seconds to 8 seconds</li> <li>In production,
 * this can help your application run on platforms like Heroku, where it must start/restart very quickly</li> </ul>
 */
public class AsyncSpringLiquibase extends DataSourceClosingSpringLiquibase {

  // named "logger" because there is already a field called "log" in "SpringLiquibase"
  private final Logger logger = LoggerFactory.getLogger(AsyncSpringLiquibase.class);

  private final Executor executor;

  private final Duration slownessThreshold;

  private final Environment env;

  private final LiquibaseProperties liquibaseProperties;

  /**
   * <p>Constructor for AsyncSpringLiquibase.</p>
   *  @param executor a {@link Executor} object.
   * @param env a {@link Environment} object.
   * @param liquibaseProperties
   */
  public AsyncSpringLiquibase(Executor executor, Environment env, LiquibaseProperties liquibaseProperties) {
    this(executor, env, liquibaseProperties, Duration.ofSeconds(5));
  }

  /**
   * <p>Constructor for AsyncSpringLiquibase.</p>
   *  @param executor a {@link Executor} object.
   * @param env a {@link Environment} object.
   * @param liquibaseProperties
   * @param slownessThreshold
   */
  protected AsyncSpringLiquibase(Executor executor, Environment env, LiquibaseProperties liquibaseProperties, Duration slownessThreshold) {
    this.executor = executor;
    this.env = env;
    this.liquibaseProperties = liquibaseProperties;
    this.slownessThreshold = slownessThreshold;
  }

  @Override
  public void afterPropertiesSet() throws LiquibaseException {
    if (!liquibaseProperties.isEnabled()) {
      logger.debug("Liquibase is disabled");
      return;
    }

    if (env.acceptsProfiles(Profiles.of("local"))) {
      // Prevent Thread Lock with spring-cloud-context GenericScope
      // https://github.com/spring-cloud/spring-cloud-commons/commit/aaa7288bae3bb4d6fdbef1041691223238d77b7b#diff-afa0715eafc2b0154475fe672dab70e4R328
      try (Connection connection = getDataSource().getConnection()) {
        executor.execute(() -> {
          try {
            logger.warn("Starting Liquibase asynchronously, your database might not be ready at startup!");
            initDb();
          } catch (LiquibaseException e) {
            logger.error("Liquibase could not start correctly, your database is NOT ready: {}", e.getMessage(), e);
          }
        });
      } catch (SQLException e) {
        logger.error("Liquibase could not start correctly, your database is NOT ready: {}", e.getMessage(), e);
      }
    } else {
      logger.debug("Starting Liquibase synchronously");
      initDb();
    }
  }

  protected void initDb() throws LiquibaseException {
    StopWatch watch = new StopWatch();
    watch.start();
    super.afterPropertiesSet();
    watch.stop();
    logger.debug("Liquibase has updated your database in {} ms", watch.getTotalTimeMillis());
    if (watch.getTotalTimeMillis() > slownessThreshold.toMillis()) {
      logger.warn("Warning, Liquibase took more than {} seconds to start up!", slownessThreshold.toSeconds());
    }
  }
}
