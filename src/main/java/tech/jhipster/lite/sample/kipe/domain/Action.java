package tech.jhipster.lite.sample.kipe.domain;

import tech.jhipster.lite.sample.error.domain.Assert;

public record Action(String action) {
  public Action {
    Assert.notBlank("action", action);
  }

  @Override
  public String toString() {
    return action();
  }
}
