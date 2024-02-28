package tech.jhipster.lite.sample.shared.kipe.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tech.jhipster.lite.sample.shared.kipe.application.TestAuthentications.*;
import static tech.jhipster.lite.sample.shared.kipe.domain.RolesAccessesFixture.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.authentication.application.UnknownAuthenticationException;
import tech.jhipster.lite.sample.shared.authentication.domain.Username;
import tech.jhipster.lite.sample.shared.error.domain.MissingMandatoryValueException;
import tech.jhipster.lite.sample.shared.kipe.domain.RolesAccessesFixture.TestResource;

@UnitTest
class LitesampleAuthorizationsTest {

  private static final LitesampleAuthorizations authorizations = new LitesampleAuthorizations(List.of(rolesAccesses()));

  @Nested
  @DisplayName("All authorized")
  class AllAuthorizedTest {

    @Test
    void shouldNotBeAuthorizedWithoutAuthentication() {
      assertThat(authorizations.allAuthorized(null, "read", TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedWithoutAction() {
      assertThat(authorizations.allAuthorized(admin(), null, TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedWithBlankAction() {
      assertThat(authorizations.allAuthorized(admin(), " ", TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedWithoutResourceAction() {
      assertThat(authorizations.allAuthorized(admin(), "read", null)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedForNotAuthorizedAction() {
      assertThat(authorizations.allAuthorized(admin(), "not-authorized", TestResource.USERS)).isFalse();
    }

    @Test
    void shouldBeAuthorizedForAuthorizedAction() {
      assertThat(authorizations.allAuthorized(admin(), "read", TestResource.USERS)).isTrue();
    }
  }

  @Nested
  @DisplayName("Get username")
  class GetUsernameTest {

    @Test
    void shouldNotGetNotAuthenticatedUserUsername() {
      assertThatThrownBy(() -> authorizations.getUsername(null)).isExactlyInstanceOf(MissingMandatoryValueException.class);
    }

    @ParameterizedTest
    @MethodSource("allValidAuthentications")
    void shouldGetAuthenticatedUserUsername(Authentication authentication) {
      assertThat(authorizations.getUsername(authentication)).isEqualTo(new Username("admin"));
    }

    @Test
    void shouldNotGetAuthenticatedUserUsernameForUnknownAuthentication() {
      assertThatThrownBy(() -> authorizations.getUsername(TestAuthentications.user(null))).isExactlyInstanceOf(
        UnknownAuthenticationException.class
      );
    }

    private static Stream<Arguments> allValidAuthentications() {
      return Stream.of(Arguments.of(createDummyAuthenticationWithPrincipalDetails("admin")), Arguments.of(TestAuthentications.admin()));
    }

    private static Authentication createDummyAuthenticationWithPrincipalDetails(String username) {
      UserDetails principalDetails = createUserDetailsWithUsername(username);
      Authentication authentication = mock(Authentication.class);
      when(authentication.getPrincipal()).thenReturn(principalDetails);
      when(authentication.toString()).thenReturn("Authentication with userDetails, username=" + username);
      return authentication;
    }

    private static UserDetails createUserDetailsWithUsername(String username) {
      UserDetails adminUserDetails = mock(UserDetails.class);
      when(adminUserDetails.getUsername()).thenReturn(username);
      return adminUserDetails;
    }
  }

  @Nested
  @DisplayName("Specific authorized")
  class SpecificAuthorizedTest {

    @Test
    void shouldNotBeAuthorizedWithoutAuthentication() {
      assertThat(authorizations.specificAuthorized(null, "read", TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedWithoutAction() {
      assertThat(authorizations.specificAuthorized(user(), null, TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedWithBlankAction() {
      assertThat(authorizations.specificAuthorized(user(), " ", TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedWithoutResourceAction() {
      assertThat(authorizations.specificAuthorized(user(), "read", null)).isFalse();
    }

    @Test
    void shouldNotBeAuthorizedForNotAuthorizedAction() {
      assertThat(authorizations.specificAuthorized(user(), "not-authorized", TestResource.USERS)).isFalse();
    }

    @Test
    void shouldBeAuthorizedForAuthorizedAction() {
      assertThat(authorizations.specificAuthorized(user(), "read", TestResource.USERS)).isTrue();
    }
  }
}
