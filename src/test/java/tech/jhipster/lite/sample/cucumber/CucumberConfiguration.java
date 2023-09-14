package tech.jhipster.lite.sample.cucumber;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.lite.sample.LitesampleApp;
import tech.jhipster.lite.sample.cucumber.CucumberConfiguration.CucumberRestTemplateConfiguration;
import tech.jhipster.lite.sample.shared.authentication.infrastructure.primary.TestSecurityConfiguration;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(
  classes = {
    LitesampleApp.class,
    TestSecurityConfiguration.class,
    CucumberAuthenticationConfiguration.class,
    CucumberRestTemplateConfiguration.class,
  },
  webEnvironment = WebEnvironment.RANDOM_PORT
)
public class CucumberConfiguration {

  @Autowired
  private TestRestTemplate rest;

  @Before
  public void resetTestContext() {
    CucumberTestContext.reset();
  }

  @Before
  public void loadInterceptors() {
    ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

    RestTemplate template = rest.getRestTemplate();
    template.setRequestFactory(requestFactory);
    template.setInterceptors(List.of(saveLastResultInterceptor()));
    template.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
  }

  private ClientHttpRequestInterceptor saveLastResultInterceptor() {
    return (request, body, execution) -> {
      ClientHttpResponse response = execution.execute(request, body);

      CucumberTestContext.addResponse(request, response, execution, body);

      return response;
    };
  }

  @TestConfiguration
  static class CucumberRestTemplateConfiguration {

    @Autowired
    private TestRestTemplate rest;

    @Bean
    CucumberRestTemplate cucumberRestTemplate() {
      return new CucumberRestTemplate(rest);
    }
  }
}
