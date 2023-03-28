package tech.jhipster.lite.sample.account.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.jhipster.lite.sample.account.infrastructure.OAuth2TokenFixture.*;

import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tech.jhipster.lite.sample.IntegrationTest;
import tech.jhipster.lite.sample.authentication.infrastructure.primary.WithUnauthenticatedMockUser;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(value = "test")
class AccountsResourceIntTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private OAuth2AuthorizedClientService authorizedClientService;

  @Autowired
  private ClientRegistration clientRegistration;

  @Test
  @WithUnauthenticatedMockUser
  void shouldNotGetAccountForNotAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/api/authenticated-user-account")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldGetAuthenticatedUserAccount() throws Exception {
    authenticatedTestUser();

    mockMvc
      .perform(get("/api/authenticated-user-account"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.username").value("user"));
  }

  private void authenticatedTestUser() {
    OAuth2AuthenticationToken authentication = testAuthenticationToken();
    registerAuthenticationToken(authentication);

    TestSecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private void registerAuthenticationToken(OAuth2AuthenticationToken authentication) {
    Map<String, Object> userDetails = authentication.getPrincipal().getAttributes();

    OAuth2AccessToken token = new OAuth2AccessToken(
      TokenType.BEARER,
      "Token",
      (Instant) userDetails.get("auth_time"),
      (Instant) userDetails.get("exp")
    );

    authorizedClientService.saveAuthorizedClient(
      new OAuth2AuthorizedClient(clientRegistration, authentication.getName(), token),
      authentication
    );
  }
}
