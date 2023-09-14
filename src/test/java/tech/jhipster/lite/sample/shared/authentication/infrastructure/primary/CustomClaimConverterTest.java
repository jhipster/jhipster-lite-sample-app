package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;

@UnitTest
@ExtendWith(MockitoExtension.class)
class CustomClaimConverterTest {

  @Mock
  private RestTemplate rest;

  private CustomClaimConverter converter;

  @BeforeEach
  void loadConverter() {
    converter = new CustomClaimConverter(buildRegistration(), rest);
  }

  private ClientRegistration buildRegistration() {
    return ClientRegistration
      .withRegistrationId("test")
      .userInfoUri("http://jhipster.test")
      .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
      .build();
  }

  @BeforeEach
  void cleanRequestAttributes() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void shouldGetClaimFromInputClaimWithoutRequestAttributes() {
    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims).containsExactly(entry("sub", "123"));
    assertNoRestClaimCall();
  }

  @Test
  void shouldGetClaimFromInputClaimWithUnhandledRequestAttributes() {
    RequestContextHolder.setRequestAttributes(new FakeRequestAttributes());

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims).containsExactly(entry("sub", "123"));
    assertNoRestClaimCall();
  }

  @Test
  void shouldNotGetClaimFromInputClaimWithTimeoutRequest() {
    mockRestTimeout();
    loadRequestAttributes();

    Throwable thrown = catchThrowable(() -> {
      converter.convert(simpleClaim());
    });

    assertThat(thrown).isInstanceOf(ResourceAccessException.class).hasCauseInstanceOf(SocketTimeoutException.class);
  }

  private ResponseEntity<ObjectNode> assertNoRestClaimCall() {
    return verify(rest, never())
      .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<ObjectNode>>any());
  }

  @Test
  void shouldConvertFullClaim() {
    user()
      .username("bob")
      .givenName("John")
      .familyName("Doe")
      .email("john.doe@company.com")
      .groups(Role.ADMIN.key(), Role.USER.key())
      .namespaceRoles(Role.ADMIN.key(), Role.USER.key())
      .mock();
    loadRequestAttributes();

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims)
      .containsEntry("sub", "123")
      .containsEntry("preferred_username", "bob")
      .containsEntry("given_name", "John")
      .containsEntry("family_name", "Doe")
      .containsEntry("email", "john.doe@company.com")
      .containsEntry("groups", List.of(Role.ADMIN.key(), Role.USER.key()))
      .containsEntry("roles", Arrays.asList(Role.ADMIN.key(), Role.USER.key()));
  }

  @Test
  void shouldConvertClaimForUnknownUser() {
    mockUser(null);
    loadRequestAttributes();

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims).containsExactly(entry("sub", "123"));
  }

  @Test
  void shouldConvertClaimForEmptyUser() {
    user().mock();
    loadRequestAttributes();

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims).containsExactly(entry("sub", "123"));
  }

  @Test
  void shouldIgnoreRolesFromAnotherNamespace() {
    user().roles(Role.ADMIN.key(), Role.USER.key()).mock();
    loadRequestAttributes();

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims).doesNotContainKey("roles");
  }

  @Test
  void shouldConvertClaimWithName() {
    user().username("bob").name("John Doe").mock();
    loadRequestAttributes();

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims)
      .containsEntry("sub", "123")
      .containsEntry("preferred_username", "bob")
      .containsEntry("given_name", "John")
      .containsEntry("family_name", "Doe");
  }

  @Test
  void shouldConvertClaimWithBlank() {
    user().username("bob").name(" ").mock();
    loadRequestAttributes();

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims)
      .containsEntry("sub", "123")
      .containsEntry("preferred_username", "bob")
      .doesNotContainKey("given_name")
      .doesNotContainKey("family_name");
  }

  @Test
  void shouldConvertClaimWithComposedName() {
    user().username("bob").name("John Doe Sr.").mock();
    loadRequestAttributes();

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims)
      .containsEntry("sub", "123")
      .containsEntry("preferred_username", "bob")
      .containsEntry("given_name", "John")
      .containsEntry("family_name", "Doe Sr.");
  }

  @Test
  void shouldGetUserFromMemoryCache() {
    user().username("bob").name("John Doe Sr.").mock();
    loadRequestAttributes();
    converter.convert(simpleClaim());

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims)
      .containsEntry("sub", "123")
      .containsEntry("preferred_username", "bob")
      .containsEntry("given_name", "John")
      .containsEntry("family_name", "Doe Sr.");
  }

  @Test
  void shouldGetFirstUserFromMemoryCache() {
    loadRequestAttributes();
    user().username("bob").name("John Doe Sr.").mock();
    converter.convert(simpleClaim());
    converter.convert(anotherSimpleClaim());

    Map<String, Object> convertedClaims = converter.convert(simpleClaim());

    assertThat(convertedClaims)
      .containsEntry("sub", "123")
      .containsEntry("preferred_username", "bob")
      .containsEntry("given_name", "John")
      .containsEntry("family_name", "Doe Sr.");
  }

  private void loadRequestAttributes() {
    ServletRequestAttributes attributes = new ServletRequestAttributes(new MockHttpServletRequest());

    RequestContextHolder.setRequestAttributes(attributes);
  }

  private static Map<String, Object> simpleClaim() {
    return Map.of("sub", "123");
  }

  private static Map<String, Object> anotherSimpleClaim() {
    return Map.of("sub", "456");
  }

  private UserBuilder user() {
    return new UserBuilder();
  }

  private class UserBuilder {

    private static final ObjectMapper json = new ObjectMapper();
    private ObjectNode user;

    private UserBuilder() {
      user = json.createObjectNode();
    }

    public UserBuilder username(String username) {
      user.put("preferred_username", username);

      return this;
    }

    public UserBuilder givenName(String givenName) {
      user.put("given_name", givenName);

      return this;
    }

    public UserBuilder familyName(String familyName) {
      user.put("family_name", familyName);

      return this;
    }

    public UserBuilder email(String email) {
      user.put("email", email);

      return this;
    }

    public UserBuilder groups(String... groups) {
      ArrayNode userGroups = user.putArray("groups");

      Arrays.asList(groups).forEach(userGroups::add);

      return this;
    }

    public UserBuilder namespaceRoles(String... roles) {
      ArrayNode userRoles = user.putArray(Claims.CLAIMS_NAMESPACE + "roles");

      Arrays.asList(roles).forEach(userRoles::add);

      return this;
    }

    public UserBuilder roles(String... roles) {
      ArrayNode userRoles = user.putArray("dummyroles");

      Arrays.asList(roles).forEach(userRoles::add);

      return this;
    }

    public UserBuilder name(String name) {
      user.put("name", name);

      return this;
    }

    public void mock() {
      mockUser(user);
    }
  }

  private void mockUser(ObjectNode user) {
    when(rest.exchange(eq("http://jhipster.test"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<ObjectNode>>any()))
      .thenReturn(ResponseEntity.ok(user));
  }

  private void mockRestTimeout() {
    when(rest.exchange(eq("http://jhipster.test"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<ObjectNode>>any()))
      .thenThrow(new ResourceAccessException(null, new SocketTimeoutException()));
  }
}
