package tech.jhipster.lite.sample.shared.kipe.application;

import org.springframework.security.core.Authentication;

/**
 * Check access for a given resource
 *
 * @param <T>
 *          Type of resource to check access on to
 */
public interface AccessChecker<T> {
  boolean can(AccessContext<T> access);

  @SuppressWarnings("unchecked")
  default boolean canOnObject(Authentication authentication, String action, Object element) {
    return can(AccessContextFactory.of(authentication, action, (T) element));
  }
}
