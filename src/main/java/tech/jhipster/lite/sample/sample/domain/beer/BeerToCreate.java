package tech.jhipster.lite.sample.sample.domain.beer;

import tech.jhipster.lite.sample.sample.domain.Amount;
import tech.jhipster.lite.sample.sample.domain.BeerId;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record BeerToCreate(BeerName name, Amount unitPrice) {
  public BeerToCreate {
    Assert.notNull("name", name);
    Assert.notNull("unitPrice", unitPrice);
  }

  public Beer create() {
    return Beer.builder().id(BeerId.newId()).name(name()).unitPrice(unitPrice()).sellingState(BeerSellingState.SOLD).build();
  }
}
