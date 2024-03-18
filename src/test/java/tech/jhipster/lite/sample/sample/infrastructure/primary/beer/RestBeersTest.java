package tech.jhipster.lite.sample.sample.infrastructure.primary.beer;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.JsonHelper;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.sample.domain.beer.Beers;
import tech.jhipster.lite.sample.sample.domain.beer.BeersFixture;

@UnitTest
class RestBeersTest {

  @Test
  void shouldSerializeToJson() {
    assertThat(JsonHelper.writeAsString(RestBeers.from(new Beers(List.of(BeersFixture.beer()))))).isEqualTo(json());
  }

  private String json() {
    return "{\"beers\":[" + RestBeerTest.json() + "]}";
  }
}
