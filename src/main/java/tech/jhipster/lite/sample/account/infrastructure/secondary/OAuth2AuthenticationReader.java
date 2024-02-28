package tech.jhipster.lite.sample.account.infrastructure.secondary;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import tech.jhipster.lite.sample.account.domain.Account;

@Service
class OAuth2AuthenticationReader {

  private static final String GIVEN_NAME = "given_name";

  public Optional<Account> authenticatedUserAccount() {
    return authenticatedUser().map(this::toAccount);
  }

  private Optional<Authentication> authenticatedUser() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
  }

  private Account toAccount(Authentication authentication) {
    Map<String, Object> attributes = readAttributes(authentication);

    return Account.builder()
      .username((String) attributes.get("preferred_username"))
      .firstname(readFirstname(attributes))
      .lastname((String) attributes.get("family_name"))
      .email((String) attributes.get("email"))
      .roles(buildRoles(authentication))
      .build();
  }

  private String readFirstname(Map<String, Object> attributes) {
    if (attributes.containsKey(GIVEN_NAME)) {
      return (String) attributes.get(GIVEN_NAME);
    }

    return (String) attributes.get("name");
  }

  private List<String> buildRoles(Authentication authentication) {
    return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
  }

  private Map<String, Object> readAttributes(Authentication authentication) {
    if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
      return oauthToken.getPrincipal().getAttributes();
    }

    if (authentication instanceof JwtAuthenticationToken jwtToken) {
      return jwtToken.getTokenAttributes();
    }

    throw new UnknownAuthenticationSchemeException();
  }
}
