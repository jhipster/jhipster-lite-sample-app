package tech.jhipster.lite.sample.cucumber;

import java.util.List;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class CucumberRestTemplate {

  private final TestRestTemplate rest;

  public CucumberRestTemplate(TestRestTemplate rest) {
    this.rest = rest;
  }

  public void get(String uri) {
    rest.getForEntity(uri, Void.class);
  }

  public void post(String uri, String content) {
    rest.exchange(uri, HttpMethod.POST, new HttpEntity<>(content, jsonHeaders()), Void.class);
  }

  public void put(String uri, String content) {
    rest.exchange(uri, HttpMethod.PUT, new HttpEntity<>(content, jsonHeaders()), Void.class);
  }

  public void delete(String uri) {
    rest.delete(uri);
  }

  public TestRestTemplate template() {
    return rest;
  }

  private HttpHeaders jsonHeaders() {
    HttpHeaders headers = new HttpHeaders();

    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    return headers;
  }
}
