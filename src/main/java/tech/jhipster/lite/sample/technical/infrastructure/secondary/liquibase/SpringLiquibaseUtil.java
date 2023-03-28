package tech.jhipster.lite.sample.technical.infrastructure.secondary.liquibase;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

/**
 * Utility class for handling SpringLiquibase.
 *
 * <p>
 * It follows implementation of
 * <a href="https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration.java">LiquibaseAutoConfiguration</a>.
 */
public final class SpringLiquibaseUtil {

  private SpringLiquibaseUtil() {}

  /**
   * <p>createSpringLiquibase.</p>
   *
   * @param liquibaseDatasource a {@link DataSource} object.
   * @param liquibaseProperties a {@link LiquibaseProperties} object.
   * @param dataSource a {@link DataSource} object.
   * @param dataSourceProperties a {@link DataSourceProperties} object.
   * @return a {@link liquibase.integration.spring.SpringLiquibase} object.
   */
  public static SpringLiquibase createSpringLiquibase(
    DataSource liquibaseDatasource,
    LiquibaseProperties liquibaseProperties,
    DataSource dataSource,
    DataSourceProperties dataSourceProperties
  ) {
    SpringLiquibase liquibase;
    DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
    if (liquibaseDataSource != null) {
      liquibase = new SpringLiquibase();
      liquibase.setDataSource(liquibaseDataSource);
      return liquibase;
    }
    liquibase = new DataSourceClosingSpringLiquibase();
    liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
    return liquibase;
  }

  /**
   * <p>createAsyncSpringLiquibase.</p>
   *
   * @param env a {@link Environment} object.
   * @param executor a {@link Executor} object.
   * @param liquibaseDatasource a {@link DataSource} object.
   * @param liquibaseProperties a {@link LiquibaseProperties} object.
   * @param dataSource a {@link DataSource} object.
   * @param dataSourceProperties a {@link DataSourceProperties} object.
   * @return a {@link AsyncSpringLiquibase} object.
   */
  public static AsyncSpringLiquibase createAsyncSpringLiquibase(
    Environment env,
    Executor executor,
    DataSource liquibaseDatasource,
    LiquibaseProperties liquibaseProperties,
    DataSource dataSource,
    DataSourceProperties dataSourceProperties
  ) {
    AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env, liquibaseProperties);
    DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
    if (liquibaseDataSource != null) {
      liquibase.setCloseDataSourceOnceMigrated(false);
      liquibase.setDataSource(liquibaseDataSource);
    } else {
      liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
    }
    return liquibase;
  }

  private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
    if (liquibaseDataSource != null) {
      return liquibaseDataSource;
    }
    if (liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null) {
      return dataSource;
    }
    return null;
  }

  private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
    String url = getProperty(liquibaseProperties::getUrl, dataSourceProperties::determineUrl);
    String user = getProperty(liquibaseProperties::getUser, dataSourceProperties::determineUsername);
    String password = getProperty(liquibaseProperties::getPassword, dataSourceProperties::determinePassword);
    return DataSourceBuilder.create().url(url).username(user).password(password).build();
  }

  private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
    return Optional.of(property).map(Supplier::get).orElseGet(defaultValue);
  }
}
