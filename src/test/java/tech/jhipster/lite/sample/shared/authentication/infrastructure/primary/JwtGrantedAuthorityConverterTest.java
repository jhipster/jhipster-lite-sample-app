package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@UnitTest
@ExtendWith(SpringExtension.class)
class JwtGrantedAuthorityConverterTest {

  @InjectMocks
  private JwtGrantedAuthorityConverter jwtGrantedAuthorityConverter;

  @Test
  void shouldConvert() {
    Jwt jwt = Jwt.withTokenValue("token")
      .header("alg", JwsAlgorithms.RS256)
      .subject("jhipster")
      .claim("roles", List.of("ROLE_ADMIN"))
      .build();

    Collection<GrantedAuthority> result = jwtGrantedAuthorityConverter.convert(jwt);

    assertThat(result).isNotEmpty().contains(new SimpleGrantedAuthority(Role.ADMIN.key()));
  }

  @Test
  void shouldConvertButEmpty() {
    Jwt jwt = Jwt.withTokenValue("token").header("alg", JwsAlgorithms.RS256).subject("jhipster").build();

    Collection<GrantedAuthority> result = jwtGrantedAuthorityConverter.convert(jwt);

    assertThat(result).isEmpty();
  }
}
