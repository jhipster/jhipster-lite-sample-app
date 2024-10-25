package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import tech.jhipster.lite.sample.UnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class ApplicationSecurityPropertiesTest {

  private static final String DEFAULT_CONTENT_SECURITY_POLICY =
    "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; img-src 'self' data:; font-src 'self' data: https://fonts.gstatic.com;";

  @Test
  void shouldGetDefaultProperties() {
    ApplicationSecurityProperties properties = new ApplicationSecurityProperties();

    assertThat(properties.getContentSecurityPolicy()).isEqualTo(DEFAULT_CONTENT_SECURITY_POLICY);
    assertThat(properties.getOauth2().getAudience()).isEmpty();
  }

  @Test
  void shouldUpdatedConfiguration() {
    ApplicationSecurityProperties properties = new ApplicationSecurityProperties();
    properties.setContentSecurityPolicy("policy");
    properties.getOauth2().setAudience(List.of("audience"));

    assertThat(properties.getContentSecurityPolicy()).isEqualTo("policy");
    assertThat(properties.getOauth2().getAudience()).containsExactly("audience");
  }
}
