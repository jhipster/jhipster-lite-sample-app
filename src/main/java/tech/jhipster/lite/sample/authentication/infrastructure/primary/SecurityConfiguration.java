package tech.jhipster.lite.sample.authentication.infrastructure.primary;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.lite.sample.authentication.domain.Role;
import tech.jhipster.lite.sample.common.domain.ExcludeFromGeneratedCodeCoverage;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

  private static final int TIMEOUT = 2000;

  private final ApplicationSecurityProperties applicationSecurityProperties;
  private final CorsFilter corsFilter;
  private final HandlerMappingIntrospector introspector;

  @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
  private String issuerUri;

  public SecurityConfiguration(
    CorsFilter corsFilter,
    ApplicationSecurityProperties applicationSecurityProperties,
    HandlerMappingIntrospector introspector
  ) {
    this.corsFilter = corsFilter;
    this.applicationSecurityProperties = applicationSecurityProperties;
    this.introspector = introspector;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web ->
      web
        .ignoring()
        .requestMatchers(antMatcher(HttpMethod.OPTIONS, "/**"))
        .requestMatchers(antMatcher("/app/**"))
        .requestMatchers(antMatcher("/i18n/**"))
        .requestMatchers(antMatcher("/content/**"))
        .requestMatchers(antMatcher("/swagger-ui/**"))
        .requestMatchers(antMatcher("/swagger-ui.html"))
        .requestMatchers(antMatcher("/v3/api-docs/**"))
        .requestMatchers(antMatcher("/test/**"));
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    return http
      .csrf(csrf -> csrf.disable())
      .addFilterBefore(corsFilter, CsrfFilter.class)
      .headers(headers -> headers
        .contentSecurityPolicy(csp -> csp.policyDirectives(applicationSecurityProperties.getContentSecurityPolicy()))
        .frameOptions(FrameOptionsConfig::sameOrigin)
        .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
        .permissionsPolicy(permissions ->
          permissions.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"))
      )
      .authorizeHttpRequests(authz -> authz
        .requestMatchers(new MvcRequestMatcher(introspector, "/api/authenticate")).permitAll()
        .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth-info")).permitAll()
        .requestMatchers(new MvcRequestMatcher(introspector, "/api/admin/**")).hasAuthority(Role.ADMIN.key())
        .requestMatchers(new MvcRequestMatcher(introspector, "/api/**")).authenticated()
        .requestMatchers(new MvcRequestMatcher(introspector, "/management/health")).permitAll()
        .requestMatchers(new MvcRequestMatcher(introspector, "/management/health/**")).permitAll()
        .requestMatchers(new MvcRequestMatcher(introspector, "/management/info")).permitAll()
        .requestMatchers(new MvcRequestMatcher(introspector, "/management/prometheus")).permitAll()
        .requestMatchers(new MvcRequestMatcher(introspector, "/management/**")).hasAuthority(Role.ADMIN.key())
        .anyRequest().authenticated()
      )
      .oauth2Login(withDefaults())
      .oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter()))
      )
      .oauth2Client(withDefaults())
      .build();
    // @formatter:on
  }

  private Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthorityConverter());

    return jwtAuthenticationConverter;
  }

  /**
   * Map authorities from "groups" or "roles" claim in ID Token.
   *
   * @return a {@link GrantedAuthoritiesMapper} that maps groups from the IdP to Spring Security Authorities.
   */
  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

      authorities.forEach(authority -> {
        // Check for OidcUserAuthority because Spring Security 5.2 returns
        // each scope as a GrantedAuthority, which we don't care about.
        if (authority instanceof OidcUserAuthority oidcUserAuthority) {
          mappedAuthorities.addAll(Claims.extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
        }
      });
      return mappedAuthorities;
    };
  }

  @Bean
  @ExcludeFromGeneratedCodeCoverage(reason = "Only called with a valid client registration repository")
  public JwtDecoder jwtDecoder(ClientRegistrationRepository clientRegistrationRepository, RestTemplateBuilder restTemplateBuilder) {
    NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

    OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(applicationSecurityProperties.getOauth2().getAudience());
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
    OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

    jwtDecoder.setJwtValidator(withAudience);
    jwtDecoder.setClaimSetConverter(
      new CustomClaimConverter(
        clientRegistrationRepository.findByRegistrationId("oidc"),
        restTemplateBuilder.setConnectTimeout(Duration.ofMillis(TIMEOUT)).setReadTimeout(Duration.ofMillis(TIMEOUT)).build()
      )
    );

    return jwtDecoder;
  }
}
