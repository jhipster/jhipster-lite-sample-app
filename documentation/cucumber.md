# Usage

In `src/test/features` create a feature file:

```
Feature: Simple WebService test
  Background:
    Given I am logged in as "user"

  Scenario: Calling WebService with static assertion
    When I get simple bean "Bob"
    Then I get simple response with name "Bob" and age 42
```

You'll then have to define the glue code:

```java
import static tech.jhipster.lite.sample.cucumber.rest.CucumberRestAssertions.*;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import tech.jhipster.lite.sample.cucumber.CucumberRestTemplate;

public class SimpleSteps {

  @Autowired
  private CucumberRestTemplate rest;

  @When("I get simple bean {string}")
  public void callSimpleWebService(String bean) {
    rest.getForEntity("/test-resources/" + bean, Void.class);
  }

  @Then("I get simple response with name {string} and age {int}")
  public void shouldGetResponse(String name, int age) {
    assertThatLastResponse().hasOkStatus().hasElement("$.name").withValue(name).and().hasElement("$.age").withValue(age);
  }
}

```

Use a `TestRestTemplate` to make your rest calls, so you'll have the `context` management: the stuff allowing easier assertions in the `Then` steps.

The `assertThatLastResponse()` is a fluent API to assert your WebServices results.

As an example, you can use results arrays in your features:

```
  Scenario: Calling WebService with line presentation table
    Given I am logged in as "user"
    When I get simple bean "Bill"
    Then I should get simple bean
      | Name | Bill |
      | Age  | 42   |

  Scenario: Calling WebServices with all users
    When I get all simple beans
    Then I should get simple beans
      | Name | Age |
      | Bob  | 42  |
      | Bill | 50  |
```

And validate them easily with those assertions:

```java
@Then("I should get simple bean")
public void shouldGetResponseContent(Map<String, Object> response) {
  assertThatLastResponse().hasResponse().containing(response);
}

@Then("I should get simple beans")
public void shouldGetResponseContent(List<Map<String, Object>> responses) {
  assertThatLastResponse().hasElement("$.users").containingExactly(responses);
}

```

## Reading responses content

Sometimes you may want to access the last response content without asserting it, you can do:

```java
CucumberRestTestContext.getElement("$.path");
CucumberRestTestContext.getElement("uri-path", "$.path");
CucumberRestTestContext.countEntries("$.path");
```

## Async services

Sometimes you have to validate the behavior of async operations. You can do:

```java
assertThatLastAsyncResponse().hasOkStatus();
```

To have a default waiting time of 5 seconds or you can get a custom max with:

```java
assertThatLastAsyncResponse(Duration.ofSeconds(30)).hasOkStatus();
```

Behind the scene, your last service will be recalled until the assertions are OK or you reach the timeout.

## Mocking beans

You may need to mock beans for your component tests, but you won't be able to do it in a "classic" way (using `@MockitoBean`) since the application context will be already loaded. A way to achieve that is to overload beans to have mocks:

```java
@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(
  classes = { JhipsterSampleApplicationApp.class, CucumberMocksConfiguration.class },
  webEnvironment = WebEnvironment.RANDOM_PORT
)
public class CucumberConfiguration {

  // other code omitted

  @TestConfiguration
  public static class CucumberMocksConfiguration {

    @Bean
    @Primary
    public AccountsRepository cucumberAccountsRepository() {
      return Mockito.mock(AccountsRepository.class);
    }
  }
}

```

**Careful: the mock bean names (by default the method name) must be different from the real one or else they may just be ignored**
