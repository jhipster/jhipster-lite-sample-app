package tech.jhipster.lite.sample.sample.domain.beer;

import tech.jhipster.lite.sample.sample.domain.BeerId;

class UnknownBeerException extends RuntimeException {

  public UnknownBeerException(BeerId id) {
    super("Beer " + id.get() + " is unknown");
  }
}
