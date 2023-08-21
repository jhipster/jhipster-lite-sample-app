package tech.jhipster.lite.sample.shared.kipe.application;

import org.springframework.security.core.Authentication;

final class AccessContextFactory {

  private AccessContextFactory() {}

  static <T> AccessContext<T> of(Authentication authentication, String action, T element) {
    if (element == null) {
      return new NullElementAccessContext<>(authentication, action);
    }

    return new ElementAccessContext<>(authentication, action, element);
  }
}
