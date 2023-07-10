package tech.jhipster.lite.sample.kipe.application;

import org.springframework.security.core.Authentication;
import tech.jhipster.lite.sample.common.domain.ExcludeFromGeneratedCodeCoverage;
import tech.jhipster.lite.sample.error.domain.Assert;

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
