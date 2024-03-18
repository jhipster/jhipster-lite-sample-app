package tech.jhipster.lite.sample.sample.infrastructure.primary.beer;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.sample.BeanValidationAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.JsonHelper;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.sample.domain.beer.BeersFixture;

@UnitTest
class RestBeerToCreateTest {

  @Test
  void shouldDeserializeFromJson() {
    assertThat(JsonHelper.readFromJson(json(), RestBeerToCreate.class).toDomain())
      .usingRecursiveComparison()
      .isEqualTo(BeersFixture.beerToCreate());
  }

  private String json() {
    return """
    {
      "name": "Cloak of feathers",
      "unitPrice": 8.53
    }
    """;
  }

  @Test
  void shouldNotValidateEmptyBean() {
    assertThatBean(new RestBeerToCreate(null, null)).hasInvalidProperty("name").and().hasInvalidProperty("unitPrice");
  }
}
