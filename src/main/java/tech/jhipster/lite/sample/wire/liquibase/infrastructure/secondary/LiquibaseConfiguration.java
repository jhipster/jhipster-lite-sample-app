package tech.jhipster.lite.sample.wire.liquibase.infrastructure.secondary;

import java.util.concurrent.Executor;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties({ LiquibaseProperties.class })
class LiquibaseConfiguration {

  private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

  private final Environment env;

  @Value("${application.liquibase.async:false}")
  private boolean async;

  @Value("${spring.liquibase.change-log}")
  private String changeLogClassPath;

  public LiquibaseConfiguration(Environment env) {
    this.env = env;
  }

  @Bean
  public SpringLiquibase liquibase(
    @Qualifier("taskExecutor") Executor executor,
    @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
    LiquibaseProperties liquibaseProperties,
    ObjectProvider<DataSource> dataSource,
    DataSourceProperties dataSourceProperties
  ) {
    SpringLiquibase liquibase;
    if (async) {
      log.debug("Configuring Liquibase with async");
      liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
        this.env,
        executor,
        liquibaseDataSource.getIfAvailable(),
        liquibaseProperties,
        dataSource.getIfUnique(),
        dataSourceProperties
      );
    } else {
      log.debug("Configuring Liquibase with sync");
      liquibase = SpringLiquibaseUtil.createSpringLiquibase(
        liquibaseDataSource.getIfAvailable(),
        liquibaseProperties,
        dataSource.getIfUnique(),
        dataSourceProperties
      );
    }
    liquibase.setChangeLog(changeLogClassPath);
    liquibase.setContexts(liquibaseProperties.getContexts());
    liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
    liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
    liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
    liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
    liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
    liquibase.setDropFirst(liquibaseProperties.isDropFirst());
    liquibase.setLabelFilter(liquibaseProperties.getLabelFilter());
    liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
    liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
    liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
    liquibase.setShouldRun(liquibaseProperties.isEnabled());

    return liquibase;
  }
}
