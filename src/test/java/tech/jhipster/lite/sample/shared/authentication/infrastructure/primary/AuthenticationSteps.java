package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import io.cucumber.java.en.Given;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import tech.jhipster.lite.sample.cucumber.CucumberAuthenticationConfiguration;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;

public class AuthenticationSteps {

  private static final Map<String, User> users = new UsersBuilder().add("admin", Role.ADMIN).add("user", Role.USER).build();

  @Autowired
  private TestRestTemplate rest;

  @Given("I am logged in as {string}")
  public void authenticateUser(String username) {
    rest.getRestTemplate().setInterceptors(interceptorsWithAuthentication(username));
  }

  private List<ClientHttpRequestInterceptor> interceptorsWithAuthentication(String user) {
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(rest.getRestTemplate().getInterceptors());

    User userToAuthenticate = users.get(user);

    assertThat(userToAuthenticate).as(unknownUserMessage(user)).isNotNull();

    interceptors.add((request, body, execution) -> {
      request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + userToAuthenticate.token());

      return execution.execute(request, body);
    });

    return interceptors;
  }

  @Given("I am logged out")
  public void logout() {
    rest.getRestTemplate().setInterceptors(interceptorsWithoutAuthentication());
  }

  private List<ClientHttpRequestInterceptor> interceptorsWithoutAuthentication() {
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(rest.getRestTemplate().getInterceptors());

    interceptors.add((request, body, execution) -> {
      request.getHeaders().remove(HttpHeaders.AUTHORIZATION);

      return execution.execute(request, body);
    });

    return interceptors;
  }

  private String unknownUserMessage(String user) {
    return "Trying to authenticate an unknown user: " + user;
  }

  private static final class UsersBuilder {

    private Map<String, User> users = new ConcurrentHashMap<>();

    public UsersBuilder add(String username, Role... roles) {
      users.put(username, new User(username, roles));

      return this;
    }

    public Map<String, User> build() {
      return Collections.unmodifiableMap(users);
    }
  }

  private static class User {

    private final Map<String, Object> claims;

    public User(String username, Role[] roles) {
      claims = Map.of("preferred_username", username, "roles", Stream.of(roles).map(Role::key).toList());
    }

    private String token() {
      return Jwts.builder()
        .setSubject("authentication")
        .signWith(CucumberAuthenticationConfiguration.JWT_KEY)
        .setClaims(claims)
        .setExpiration(Date.from(Instant.now().plusSeconds(300)))
        .compact();
    }
  }
}
