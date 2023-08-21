package tech.jhipster.lite.sample.dummy.domain.beer;

import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record BeerName(String name) {
  public BeerName {
    Assert.field("name", name).notBlank().maxLength(255);
  }

  public String get() {
    return name();
  }
}
