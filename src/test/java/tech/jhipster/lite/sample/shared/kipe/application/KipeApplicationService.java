package tech.jhipster.lite.sample.shared.kipe.application;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.jhipster.lite.sample.shared.kipe.domain.KipeDummy;

@Service
public class KipeApplicationService {

  @PreAuthorize("can('update', #dummy)")
  public void update(KipeDummy dummy) {
    // Nothing to do
  }
}
