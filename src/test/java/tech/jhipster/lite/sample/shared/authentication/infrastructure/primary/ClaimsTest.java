package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;

@UnitTest
class ClaimsTest {

  private static final String CLAIMS_NAMESPACE = "https://www.jhipster.tech/";

  @Test
  void shouldExtractAuthorityFromClaims() {
    Map<String, Object> claims = Map.of("groups", List.of(Role.ADMIN.key(), Role.USER.key()));

    List<GrantedAuthority> authorities = Claims.extractAuthorityFromClaims(claims);

    assertThat(authorities).containsExactly(new SimpleGrantedAuthority(Role.ADMIN.key()), new SimpleGrantedAuthority(Role.USER.key()));
  }

  @Test
  void shouldExtractAuthorityFromClaimsNamespacedRoles() {
    Map<String, Object> claims = Map.of(CLAIMS_NAMESPACE + "roles", List.of(Role.ADMIN.key(), Role.USER.key()));

    List<GrantedAuthority> authorities = Claims.extractAuthorityFromClaims(claims);

    assertThat(authorities).containsExactly(new SimpleGrantedAuthority(Role.ADMIN.key()), new SimpleGrantedAuthority(Role.USER.key()));
  }
}
