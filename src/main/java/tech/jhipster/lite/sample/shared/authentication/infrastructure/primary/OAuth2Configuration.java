package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
class OAuth2Configuration {

  @Bean
  @SuppressWarnings("java:S1874")
  public OAuth2AuthorizedClientManager authorizedClientManager(
    ClientRegistrationRepository clientRegistrationRepository,
    OAuth2AuthorizedClientRepository authorizedClientRepository
  ) {
    DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
      clientRegistrationRepository,
      authorizedClientRepository
    );

    authorizedClientManager.setAuthorizedClientProvider(
      OAuth2AuthorizedClientProviderBuilder.builder()
        .authorizationCode()
        .refreshToken(builder -> builder.clockSkew(Duration.ofMinutes(1)))
        .clientCredentials()
        .build()
    );

    return authorizedClientManager;
  }
}
