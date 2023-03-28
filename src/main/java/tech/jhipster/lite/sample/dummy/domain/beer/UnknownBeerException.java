package tech.jhipster.lite.sample.dummy.domain.beer;

import tech.jhipster.lite.sample.dummy.domain.BeerId;

class UnknownBeerException extends RuntimeException {

  public UnknownBeerException(BeerId id) {
    super("Beer " + id.get() + " is unknown");
  }
}
