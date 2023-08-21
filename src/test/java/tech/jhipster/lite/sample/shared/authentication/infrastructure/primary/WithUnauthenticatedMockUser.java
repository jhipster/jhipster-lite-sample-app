package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@WithSecurityContext(factory = WithUnauthenticatedMockUser.Factory.class)
public @interface WithUnauthenticatedMockUser {
  class Factory implements WithSecurityContextFactory<WithUnauthenticatedMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithUnauthenticatedMockUser annotation) {
      return SecurityContextHolder.createEmptyContext();
    }
  }
}
