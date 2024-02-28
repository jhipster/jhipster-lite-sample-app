package tech.jhipster.lite.sample.dummy.domain.order;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.sample.dummy.domain.order.BeerOrderFixture.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.dummy.domain.Amount;
import tech.jhipster.lite.sample.shared.error.domain.MissingMandatoryValueException;

@UnitTest
class BeerOrderTest {

  @Test
  void shouldNotBuildEmptyOrder() {
    assertThatThrownBy(() -> BeerOrder.builder().build()).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldGetConsolidatedOrderLines() {
    BeerOrder order = beerOrder();

    assertThat(order.lines()).containsExactlyInAnyOrder(
      new BeerOrderLine(orderedCloakOfFeather(), 2),
      new BeerOrderLine(orderedAnteMeridiem(), 3)
    );
  }

  @Test
  void shouldGetOrderTotal() {
    assertThat(beerOrder().total()).isEqualTo(new Amount(new BigDecimal("33.44")));
  }
}
