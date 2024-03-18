package tech.jhipster.lite.sample.sample.domain.order;

import tech.jhipster.lite.sample.sample.domain.Amount;
import tech.jhipster.lite.sample.sample.domain.BeerId;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record OrderedBeer(BeerId beer, Amount unitPrice) {
  public OrderedBeer {
    Assert.notNull("beer", beer);
    Assert.notNull("unitPrice", unitPrice);
  }
}
