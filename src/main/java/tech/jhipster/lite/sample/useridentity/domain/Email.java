package tech.jhipster.lite.sample.useridentity.domain;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import tech.jhipster.lite.sample.error.domain.Assert;

public record Email(String email) {
  public Email {
    Assert.field("email", email).notBlank().maxLength(255);
  }

  public static Optional<Email> of(String email) {
    return Optional.ofNullable(email).filter(StringUtils::isNotBlank).map(Email::new);
  }

  public String get() {
    return email();
  }
}
