package tech.jhipster.lite.sample.kipe.application;

import org.springframework.stereotype.Component;
import tech.jhipster.lite.sample.kipe.domain.KipeDummy;

@Component
class KipeDummyAccessChecker implements AccessChecker<KipeDummy> {

  @Override
  public boolean can(AccessContext<KipeDummy> access) {
    return access.authentication() != null && access.element().value().equals("authorized");
  }
}
