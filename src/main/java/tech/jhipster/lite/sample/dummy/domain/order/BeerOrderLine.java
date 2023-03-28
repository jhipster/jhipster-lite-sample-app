package tech.jhipster.lite.sample.dummy.domain.order;

import tech.jhipster.lite.sample.dummy.domain.Amount;
import tech.jhipster.lite.sample.error.domain.Assert;

public record BeerOrderLine(OrderedBeer beer, int quantity) {
  public BeerOrderLine {
    Assert.notNull("beer", beer);
    Assert.field("quantity", quantity).min(1);
  }

  Amount amount() {
    return beer().unitPrice().times(quantity());
  }
}
