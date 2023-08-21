package tech.jhipster.lite.sample.shared.kipe.application;

import org.springframework.security.core.Authentication;
import tech.jhipster.lite.sample.shared.error.domain.Assert;
import tech.jhipster.lite.sample.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

@ExcludeFromGeneratedCodeCoverage(reason = "Untested null object structure")
record NullElementAccessContext<T>(Authentication authentication, String action) implements AccessContext<T> {
  public NullElementAccessContext {
    Assert.notNull("authentication", authentication);
    Assert.notBlank("action", action);
  }

  @Override
  public T element() {
    return null;
  }
}
