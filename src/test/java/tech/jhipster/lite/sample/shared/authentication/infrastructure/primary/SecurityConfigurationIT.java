package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import tech.jhipster.lite.sample.IntegrationTest;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;

@IntegrationTest
class SecurityConfigurationIT {

  @Autowired
  private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

  @Test
  void shouldUserAuthoritiesMapperWithOidcUserAuthority() {
    Map<String, Object> claims = new HashMap<>();
    claims.put("groups", List.of(Role.USER.key()));
    claims.put("sub", 123);
    claims.put("preferred_username", "admin");
    OidcIdToken idToken = new OidcIdToken(ID_TOKEN, Instant.now(), Instant.now().plusSeconds(60), claims);

    OidcUserInfo userInfo = new OidcUserInfo(claims);

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new OidcUserAuthority(Role.USER.key(), idToken, userInfo));

    assertThatCode(() -> grantedAuthoritiesMapper.mapAuthorities(authorities)).doesNotThrowAnyException();
  }

  @Test
  void shouldUserAuthoritiesMapperWithSimpleGrantedAuthority() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(Role.USER.key()));

    assertThatCode(() -> grantedAuthoritiesMapper.mapAuthorities(authorities)).doesNotThrowAnyException();
  }
}
