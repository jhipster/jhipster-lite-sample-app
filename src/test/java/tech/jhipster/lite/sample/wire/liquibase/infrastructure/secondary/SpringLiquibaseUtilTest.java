package tech.jhipster.lite.sample.wire.liquibase.infrastructure.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class SpringLiquibaseUtilTest {

  @Test
  void createSpringLiquibaseFromLiquibaseDataSource() {
    DataSource liquibaseDatasource = DataSourceBuilder.create().url("jdbc:h2:mem:liquibase").username("sa").build();
    LiquibaseProperties liquibaseProperties = null;
    DataSource normalDataSource = null;
    DataSourceProperties dataSourceProperties = null;

    SpringLiquibase liquibase = SpringLiquibaseUtil.createSpringLiquibase(
      liquibaseDatasource,
      liquibaseProperties,
      normalDataSource,
      dataSourceProperties
    );
    assertThat(liquibase)
      .isNotInstanceOf(DataSourceClosingSpringLiquibase.class)
      .extracting(SpringLiquibase::getDataSource)
      .isEqualTo(liquibaseDatasource)
      .asInstanceOf(type(HikariDataSource.class))
      .hasFieldOrPropertyWithValue("jdbcUrl", "jdbc:h2:mem:liquibase")
      .hasFieldOrPropertyWithValue("username", "sa")
      .hasFieldOrPropertyWithValue("password", null);
  }

  @Test
  void createSpringLiquibaseFromNormalDataSource() {
    DataSource liquibaseDatasource = null;
    LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
    DataSource normalDataSource = DataSourceBuilder.create().url("jdbc:h2:mem:normal").username("sa").build();
    DataSourceProperties dataSourceProperties = null;

    SpringLiquibase liquibase = SpringLiquibaseUtil.createSpringLiquibase(
      liquibaseDatasource,
      liquibaseProperties,
      normalDataSource,
      dataSourceProperties
    );
    assertThat(liquibase)
      .isNotInstanceOf(DataSourceClosingSpringLiquibase.class)
      .extracting(SpringLiquibase::getDataSource)
      .isEqualTo(normalDataSource)
      .asInstanceOf(type(HikariDataSource.class))
      .hasFieldOrPropertyWithValue("jdbcUrl", "jdbc:h2:mem:normal")
      .hasFieldOrPropertyWithValue("username", "sa")
      .hasFieldOrPropertyWithValue("password", null);
  }

  @Test
  void createSpringLiquibaseFromLiquibaseProperties() {
    DataSource liquibaseDatasource = null;
    LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
    liquibaseProperties.setUrl("jdbc:h2:mem:liquibase");
    liquibaseProperties.setUser("sa");
    DataSource normalDataSource = null;
    DataSourceProperties dataSourceProperties = new DataSourceProperties();
    dataSourceProperties.setPassword("password");

    SpringLiquibase liquibase = SpringLiquibaseUtil.createSpringLiquibase(
      liquibaseDatasource,
      liquibaseProperties,
      normalDataSource,
      dataSourceProperties
    );
    assertThat(liquibase)
      .asInstanceOf(type(DataSourceClosingSpringLiquibase.class))
      .extracting(SpringLiquibase::getDataSource)
      .asInstanceOf(type(HikariDataSource.class))
      .hasFieldOrPropertyWithValue("jdbcUrl", "jdbc:h2:mem:liquibase")
      .hasFieldOrPropertyWithValue("username", "sa")
      .hasFieldOrPropertyWithValue("password", "password");
  }

  @Test
  void createAsyncSpringLiquibaseFromLiquibaseDataSource() {
    DataSource liquibaseDatasource = DataSourceBuilder.create().url("jdbc:h2:mem:liquibase").username("sa").build();
    LiquibaseProperties liquibaseProperties = null;
    DataSource normalDataSource = null;
    DataSourceProperties dataSourceProperties = null;

    AsyncSpringLiquibase liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
      null,
      null,
      liquibaseDatasource,
      liquibaseProperties,
      normalDataSource,
      dataSourceProperties
    );
    assertThat(liquibase.getDataSource())
      .isEqualTo(liquibaseDatasource)
      .asInstanceOf(type(HikariDataSource.class))
      .hasFieldOrPropertyWithValue("jdbcUrl", "jdbc:h2:mem:liquibase")
      .hasFieldOrPropertyWithValue("username", "sa")
      .hasFieldOrPropertyWithValue("password", null);
  }

  @Test
  void createAsyncSpringLiquibaseFromNormalDataSource() {
    DataSource liquibaseDatasource = null;
    LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
    DataSource normalDataSource = DataSourceBuilder.create().url("jdbc:h2:mem:normal").username("sa").build();
    DataSourceProperties dataSourceProperties = null;

    AsyncSpringLiquibase liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
      null,
      null,
      liquibaseDatasource,
      liquibaseProperties,
      normalDataSource,
      dataSourceProperties
    );
    assertThat(liquibase.getDataSource())
      .isEqualTo(normalDataSource)
      .asInstanceOf(type(HikariDataSource.class))
      .hasFieldOrPropertyWithValue("jdbcUrl", "jdbc:h2:mem:normal")
      .hasFieldOrPropertyWithValue("username", "sa")
      .hasFieldOrPropertyWithValue("password", null);
  }

  @Test
  void createAsyncSpringLiquibaseFromLiquibaseProperties() {
    DataSource liquibaseDatasource = null;
    LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
    liquibaseProperties.setUrl("jdbc:h2:mem:liquibase");
    liquibaseProperties.setUser("sa");
    DataSource normalDataSource = null;
    DataSourceProperties dataSourceProperties = new DataSourceProperties();
    dataSourceProperties.setPassword("password");

    AsyncSpringLiquibase liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
      null,
      null,
      liquibaseDatasource,
      liquibaseProperties,
      normalDataSource,
      dataSourceProperties
    );
    assertThat(liquibase.getDataSource())
      .asInstanceOf(type(HikariDataSource.class))
      .hasFieldOrPropertyWithValue("jdbcUrl", "jdbc:h2:mem:liquibase")
      .hasFieldOrPropertyWithValue("username", "sa")
      .hasFieldOrPropertyWithValue("password", "password");
  }

  @Test
  void shouldNotCreateAsyncSpringLiquibaseFromLiquibasePropertiesWithNullUser() {
    DataSource liquibaseDatasource = null;
    LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
    liquibaseProperties.setUrl("jdbc:h2:mem:liquibase");
    liquibaseProperties.setUser(null);
    DataSource normalDataSource = null;
    DataSourceProperties dataSourceProperties = new DataSourceProperties();
    dataSourceProperties.setPassword("password");

    assertThatThrownBy(
      () ->
        SpringLiquibaseUtil.createAsyncSpringLiquibase(
          null,
          null,
          liquibaseDatasource,
          liquibaseProperties,
          normalDataSource,
          dataSourceProperties
        )
    ).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldNotCreateAsyncSpringLiquibaseFromLiquibasePropertiesWithNullUrl() {
    DataSource liquibaseDatasource = null;
    LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
    liquibaseProperties.setUrl(null);
    liquibaseProperties.setUser("sa");
    DataSource normalDataSource = null;
    DataSourceProperties dataSourceProperties = new DataSourceProperties();
    dataSourceProperties.setPassword("password");

    assertThatThrownBy(
      () ->
        SpringLiquibaseUtil.createAsyncSpringLiquibase(
          null,
          null,
          liquibaseDatasource,
          liquibaseProperties,
          normalDataSource,
          dataSourceProperties
        )
    ).isExactlyInstanceOf(NullPointerException.class);
  }
}
