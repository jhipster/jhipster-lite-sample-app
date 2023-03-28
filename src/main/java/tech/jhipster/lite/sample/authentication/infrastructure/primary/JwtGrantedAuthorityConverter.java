package tech.jhipster.lite.sample.authentication.infrastructure.primary;

import java.util.Collection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
class JwtGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  public JwtGrantedAuthorityConverter() {
    // Bean extracting authority.
  }

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    return Claims.extractAuthorityFromClaims(jwt.getClaims());
  }
}
