package tech.jhipster.lite.sample.shared.authentication.application;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.*;
import static tech.jhipster.lite.sample.shared.authentication.application.AuthenticatedUser.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;
import tech.jhipster.lite.sample.shared.authentication.domain.Username;

@UnitTest
class AuthenticatedUserTest {

  @BeforeEach
  @AfterEach
  void cleanup() {
    SecurityContextHolder.clearContext();
  }

  @Nested
  @DisplayName("Username")
  class AuthenticatedUserUsernameTest {

    @Test
    void shouldNotGetNotAuthenticatedUserUsername() {
      assertThatThrownBy(AuthenticatedUser::username).isExactlyInstanceOf(NotAuthenticatedUserException.class);
    }

    @Test
    void shouldNotGetUsernameForUnknownAuthentication() {
      authenticate(new TestingAuthenticationToken(null, null));

      assertThatThrownBy(AuthenticatedUser::username).isExactlyInstanceOf(UnknownAuthenticationException.class);
    }

    @Test
    void shouldNotGetUsernameForOAuthUserWithoutUsername() {
      authenticate(oAuth2AuthenticationTokenWithoutUsername());

      assertThatThrownBy(AuthenticatedUser::username).isExactlyInstanceOf(NotAuthenticatedUserException.class);
    }

    @ParameterizedTest
    @MethodSource("allValidAuthentications")
    void shouldGetAuthenticatedUserUsername(Authentication authentication) {
      authenticate(authentication);

      assertThat(username()).isEqualTo(new Username("admin"));
    }

    @Test
    void shouldGetEmptyAuthenticatedUsernameForNotAuthenticatedUser() {
      assertThat(optionalUsername()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("allValidAuthentications")
    void shouldGetOptionalAuthenticatedUserUsername(Authentication authentication) {
      authenticate(authentication);

      assertThat(optionalUsername()).contains(new Username("admin"));
    }

    @Test
    void shouldNotGetOptionalUsernameForUnknownAuthentication() {
      authenticate(new TestingAuthenticationToken(null, null));

      assertThatThrownBy(AuthenticatedUser::optionalUsername).isExactlyInstanceOf(UnknownAuthenticationException.class);
    }

    private static Stream<Arguments> allValidAuthentications() {
      return Stream.of(
        Arguments.of(usernamePasswordAuthenticationToken()),
        Arguments.of(oAuth2AuthenticationTokenWithUsername()),
        Arguments.of(jwtAuthenticationToken()),
        Arguments.of(new UsernamePasswordAuthenticationToken("admin", "admin"))
      );
    }
  }

  @Nested
  @DisplayName("Roles")
  class AuthenticatedUserRolesTest {

    @Test
    void shouldGetEmptyRolesForNotAuthenticatedUser() {
      assertThat(roles().hasRole()).isFalse();
    }

    @Test
    void shouldGetRolesFromClaim() {
      authenticate(oAuth2AuthenticationTokenWithUsername());

      assertThat(roles().get()).containsExactly(Role.USER);
    }
  }

  @Nested
  @DisplayName("Attributes")
  class AuthenticatedUserAttributesTest {

    @Test
    @DisplayName("should get attributes for OAuth2")
    void shouldGetAttributesForOAuth2() {
      authenticate(oAuth2AuthenticationTokenWithUsername());

      assertThat(attributes()).containsEntry("preferred_username", "admin");
    }

    @Test
    @DisplayName("should get attributes for JWT")
    void shouldGetAttributesForJWT() {
      authenticate(jwtAuthenticationToken());

      assertThat(attributes()).containsEntry("preferred_username", "admin");
    }

    @Test
    void shouldNotGetAttributesForAnotherToken() {
      authenticate(usernamePasswordAuthenticationToken());

      assertThatThrownBy(AuthenticatedUser::attributes).isExactlyInstanceOf(UnknownAuthenticationException.class);
    }

    @Test
    void shouldNotGetAttributesForNotAuthenticatedUser() {
      assertThatThrownBy(AuthenticatedUser::attributes).isExactlyInstanceOf(NotAuthenticatedUserException.class);
    }
  }

  private static OAuth2AuthenticationToken oAuth2AuthenticationTokenWithUsername() {
    return oAuth2AuthenticationToken(Map.of("groups", Role.USER.key(), "sub", 123, "preferred_username", "admin"));
  }

  private static OAuth2AuthenticationToken oAuth2AuthenticationTokenWithoutUsername() {
    return oAuth2AuthenticationToken(Map.of("groups", Role.USER.key(), "sub", 123));
  }

  private static OAuth2AuthenticationToken oAuth2AuthenticationToken(Map<String, Object> claims) {
    OidcIdToken idToken = new OidcIdToken(ID_TOKEN, Instant.now(), Instant.now().plusSeconds(60), claims);

    Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.key()));
    OidcUser user = new DefaultOidcUser(authorities, idToken);

    return new OAuth2AuthenticationToken(user, authorities, "oidc");
  }

  private static JwtAuthenticationToken jwtAuthenticationToken() {
    Jwt jwt = Jwt.withTokenValue("token")
      .header("alg", JwsAlgorithms.RS256)
      .subject("jhipster")
      .claim("preferred_username", "admin")
      .build();

    return new JwtAuthenticationToken(jwt, adminAuthorities());
  }

  private static UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken() {
    Collection<GrantedAuthority> authorities = adminAuthorities();
    var user = new User("admin", "admin", authorities);

    return new UsernamePasswordAuthenticationToken(user, "admin", authorities);
  }

  private static List<GrantedAuthority> adminAuthorities() {
    return List.of(new SimpleGrantedAuthority(Role.ADMIN.key()));
  }

  private void authenticate(Authentication token) {
    var securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(token);
    SecurityContextHolder.setContext(securityContext);
  }
}
