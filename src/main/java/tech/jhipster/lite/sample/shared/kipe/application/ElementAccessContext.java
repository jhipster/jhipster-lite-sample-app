package tech.jhipster.lite.sample.shared.kipe.application;

import org.springframework.security.core.Authentication;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record ElementAccessContext<T>(Authentication authentication, String action, T element) implements AccessContext<T> {
  public ElementAccessContext {
    Assert.notNull("authentication", authentication);
    Assert.notBlank("action", action);
    Assert.notNull("element", element);
  }
}
