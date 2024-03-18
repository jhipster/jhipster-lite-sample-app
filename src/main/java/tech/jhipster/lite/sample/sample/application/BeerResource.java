package tech.jhipster.lite.sample.sample.application;

import tech.jhipster.lite.sample.shared.kipe.domain.Resource;

enum BeerResource implements Resource {
  BEERS("beers");

  private final String key;

  BeerResource(String key) {
    this.key = key;
  }

  @Override
  public String key() {
    return key;
  }
}
