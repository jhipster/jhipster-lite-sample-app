package tech.jhipster.lite.sample.sample.domain.beer;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.sample.sample.domain.BeersIdentityFixture.*;
import static tech.jhipster.lite.sample.sample.domain.beer.BeersFixture.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class BeersTest {

  @Test
  void shouldSortBeersByNames() {
    Beer anteMeridiem = Beer.builder()
      .id(anteMeridiemId())
      .name(new BeerName("Ante Meridiem"))
      .unitPrice(anteMeridiemUnitPrice())
      .sellingState(BeerSellingState.SOLD)
      .build();

    Beers beers = new Beers(List.of(beer(), anteMeridiem));

    assertThat(beers.get()).usingRecursiveFieldByFieldElementComparator().containsExactly(anteMeridiem, beer());
  }
}
