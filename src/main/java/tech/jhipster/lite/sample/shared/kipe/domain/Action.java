package tech.jhipster.lite.sample.shared.kipe.domain;

import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record Action(String action) {
  public Action {
    Assert.notBlank("action", action);
  }

  @Override
  public String toString() {
    return action();
  }
}
