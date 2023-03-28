package tech.jhipster.lite.sample;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

@UnitTest
class ApplicationStartupTracesTest {

  private static final String SEPARATOR = "-".repeat(58);

  @Test
  void shouldBuildTraceForEmptyNonWebEnvironment() {
    assertThat(ApplicationStartupTraces.of(new MockEnvironment()))
      .contains("  Application is running!")
      .doesNotContain("Local")
      .doesNotContain("External")
      .doesNotContain("Profile(s)")
      .doesNotContain("Config Server:");
  }

  @Test
  void shouldBuildTraceForEmptyWebEnvironment() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("server.port", "80");

    assertThat(ApplicationStartupTraces.of(environment))
      .contains("  Application is running!")
      .contains("  Local: \thttp://localhost:80/")
      .containsPattern("  External: \thttp://[^:]+:80/");
  }

  @Test
  void shouldBuildTraceForApplicationWithWebConfiguration() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("server.ssl.key-store", "key");
    environment.setProperty("server.port", "8080");
    environment.setProperty("server.servlet.context-path", "/custom-path");
    environment.setProperty("configserver.status", "config");
    environment.setActiveProfiles("local", "mongo");

    assertThat(ApplicationStartupTraces.of(environment))
      .contains("  Local: \thttps://localhost:8080/custom-path")
      .containsPattern("  External: \thttps://[^:]+:8080/custom-path")
      .contains("Profile(s): \tlocal, mongo")
      .contains(SEPARATOR + "\n  Config Server: config\n" + SEPARATOR);
  }

  @Test
  void shouldBuildTraceForEnvironmentWithApplicationName() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("spring.application.name", "jhlite");

    assertThat(ApplicationStartupTraces.of(environment)).contains("  Application 'jhlite' is running!");
  }
}
