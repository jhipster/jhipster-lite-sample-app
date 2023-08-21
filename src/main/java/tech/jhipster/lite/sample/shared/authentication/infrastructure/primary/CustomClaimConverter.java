package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.jhipster.lite.sample.shared.memoizer.domain.Memoizers;

/**
 * Claim converter to add custom claims by retrieving the user from the userinfo endpoint.
 */
class CustomClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {

  private static final String GIVEN_NAME = "given_name";
  private static final String FAMILY_NAME = "family_name";
  private static final String EMAIL = "email";
  private static final String GROUPS = "groups";
  private static final String NAME = "name";
  private static final String PREFERRED_USERNAME = "preferred_username";
  private static final String ROLES = "roles";
  private static final String SUB = "sub";

  private static final List<ClaimAppender> CLAIM_APPENDERS = buildAppenders();

  private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
  private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
  private final Function<SubAttributes, ObjectNode> users;

  public CustomClaimConverter(ClientRegistration registration, RestTemplate restTemplate) {
    this.users = Memoizers.of(subAttributes -> loadUser(registration, restTemplate, subAttributes));
  }

  private static List<ClaimAppender> buildAppenders() {
    return List.of(
      new StandardClaimAppender(PREFERRED_USERNAME),
      new StandardClaimAppender(GIVEN_NAME),
      new StandardClaimAppender(FAMILY_NAME),
      new StandardClaimAppender(EMAIL),
      new NameClaimAppender(),
      new GroupClaimAppender(),
      new RolesClaimAppender()
    );
  }

  public Map<String, Object> convert(Map<String, Object> claims) {
    Map<String, Object> convertedClaims = this.delegate.convert(claims);

    if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
      ObjectNode user = getUser(claims, attributes);

      appendCustomClaim(convertedClaims, user);
    }

    return convertedClaims;
  }

  private ObjectNode getUser(Map<String, Object> claims, ServletRequestAttributes attributes) {
    return users.apply(new SubAttributes(claims.get(SUB).toString(), attributes));
  }

  private ObjectNode loadUser(ClientRegistration registration, RestTemplate restTemplate, SubAttributes subAttributes) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, buildBearer(getToken(subAttributes.attributes)));

    ResponseEntity<ObjectNode> userInfo = restTemplate.exchange(
      registration.getProviderDetails().getUserInfoEndpoint().getUri(),
      HttpMethod.GET,
      new HttpEntity<String>(headers),
      ObjectNode.class
    );

    return userInfo.getBody();
  }

  private String getToken(ServletRequestAttributes attributes) {
    return bearerTokenResolver.resolve(attributes.getRequest());
  }

  private String buildBearer(String token) {
    return "Bearer " + token;
  }

  private void appendCustomClaim(Map<String, Object> claim, ObjectNode user) {
    if (user == null) {
      return;
    }

    CLAIM_APPENDERS.stream().forEach(appender -> appender.append(claim, user));
  }

  private static interface ClaimAppender {
    public void append(Map<String, Object> claim, ObjectNode user);
  }

  private static class StandardClaimAppender implements ClaimAppender {

    private final String key;

    public StandardClaimAppender(String key) {
      this.key = key;
    }

    @Override
    public void append(Map<String, Object> claim, ObjectNode user) {
      if (user.has(key)) {
        claim.put(key, user.get(key).asText());
      }
    }
  }

  private static class NameClaimAppender implements ClaimAppender {

    @Override
    public void append(Map<String, Object> claim, ObjectNode user) {
      // Allow full name in a name claim - happens with Auth0
      if (user.has(NAME)) {
        String[] name = user.get(NAME).asText().split("\\s+");

        if (name.length > 0) {
          claim.put(GIVEN_NAME, name[0]);
          claim.put(FAMILY_NAME, String.join(" ", Arrays.copyOfRange(name, 1, name.length)));
        }
      }
    }
  }

  private static class GroupClaimAppender implements ClaimAppender {

    @Override
    public void append(Map<String, Object> claim, ObjectNode user) {
      if (user.has(GROUPS)) {
        List<String> groups = buildList(user.get(GROUPS));

        claim.put(GROUPS, groups);
      }
    }
  }

  private static class RolesClaimAppender implements ClaimAppender {

    @Override
    public void append(Map<String, Object> claim, ObjectNode user) {
      if (user.has(Claims.CLAIMS_NAMESPACE + ROLES)) {
        List<String> roles = buildList(user.get(Claims.CLAIMS_NAMESPACE + ROLES));

        claim.put(ROLES, roles);
      }
    }
  }

  private static List<String> buildList(JsonNode node) {
    return StreamSupport.stream(node.spliterator(), false).map(JsonNode::asText).toList();
  }

  private record SubAttributes(String sub, ServletRequestAttributes attributes) {}
}
