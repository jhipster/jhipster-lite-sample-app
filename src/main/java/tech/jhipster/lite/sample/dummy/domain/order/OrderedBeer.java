package tech.jhipster.lite.sample.dummy.domain.order;

import tech.jhipster.lite.sample.dummy.domain.Amount;
import tech.jhipster.lite.sample.dummy.domain.BeerId;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record OrderedBeer(BeerId beer, Amount unitPrice) {
  public OrderedBeer {
    Assert.notNull("beer", beer);
    Assert.notNull("unitPrice", unitPrice);
  }
}
