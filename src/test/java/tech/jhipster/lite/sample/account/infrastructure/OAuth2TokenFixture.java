package tech.jhipster.lite.sample.account.infrastructure;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import tech.jhipster.lite.sample.authentication.domain.Role;

public final class OAuth2TokenFixture {

  private static final String TOKEN_ID =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
    ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsIm" +
    "p0aSI6ImQzNWRmMTRkLTA5ZjYtNDhmZi04YTkzLTdjNmYwMzM5MzE1OSIsImlhdCI6MTU0M" +
    "Tk3MTU4MywiZXhwIjoxNTQxOTc1MTgzfQ.QaQOarmV8xEUYV7yvWzX3cUE_4W1luMcWCwpr" +
    "oqqUrg";

  private OAuth2TokenFixture() {}

  public static OAuth2AuthenticationToken testAuthenticationToken() {
    return buildToken(testAuthenticationClaims());
  }

  public static Map<String, Object> testAuthenticationClaims() {
    Map<String, Object> claims = new HashMap<>();

    claims.put("preferred_username", "user");
    claims.put("roles", List.of(Role.ADMIN.key()));
    claims.put("email", "email@company.fr");
    claims.put("given_name", "Paul");
    claims.put("family_name", "DUPOND");

    return claims;
  }

  public static OAuth2AuthenticationToken buildToken(Map<String, Object> claims) {
    Instant now = Instant.now();

    OidcIdToken token = new OidcIdToken(TOKEN_ID, now, now.plusSeconds(300), claims);
    List<SimpleGrantedAuthority> authorities = adminAuthorities();
    DefaultOidcUser user = new DefaultOidcUser(authorities, token, new OidcUserInfo(claims), "preferred_username");

    return new OAuth2AuthenticationToken(user, authorities, "oidc");
  }

  public static JwtAuthenticationToken testJwtAuthenticationToken() {
    Jwt.Builder jwt = Jwt.withTokenValue("token-just-for-drinking-beers").header("alg", JwsAlgorithms.RS256).subject("jhipster");
    testAuthenticationClaims().forEach(jwt::claim);
    return new JwtAuthenticationToken(jwt.build(), adminAuthorities());
  }

  private static List<SimpleGrantedAuthority> adminAuthorities() {
    return List.of(new SimpleGrantedAuthority(Role.ADMIN.key()));
  }
}
