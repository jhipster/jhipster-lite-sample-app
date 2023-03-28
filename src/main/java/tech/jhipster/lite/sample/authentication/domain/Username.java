package tech.jhipster.lite.sample.authentication.domain;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import tech.jhipster.lite.sample.error.domain.Assert;

public record Username(String username) {
  public Username {
    Assert.field("username", username).notBlank().maxLength(100);
  }

  public String get() {
    return username();
  }

  public static Optional<Username> of(String username) {
    return Optional.ofNullable(username).filter(StringUtils::isNotBlank).map(Username::new);
  }
}
