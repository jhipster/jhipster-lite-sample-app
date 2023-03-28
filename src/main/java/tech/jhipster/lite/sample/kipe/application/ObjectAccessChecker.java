package tech.jhipster.lite.sample.kipe.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.jhipster.lite.sample.common.domain.Generated;

@Component
class ObjectAccessChecker implements AccessChecker<Object> {

  private static final Logger logger = LoggerFactory.getLogger(ObjectAccessChecker.class);

  @Override
  @Generated(reason = "Ignored log conditional")
  public boolean can(AccessContext<Object> access) {
    if (logger.isWarnEnabled()) {
      logger.warn("No access checker found for {}, using default for action {}", getElementClass(access.element()), access.action());
    }

    return false;
  }

  private String getElementClass(Object item) {
    if (item == null) {
      return "unknown";
    }

    return item.getClass().getName();
  }
}
