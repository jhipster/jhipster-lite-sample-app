package tech.jhipster.lite.sample.technical.infrastructure.primary.redirection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tech.jhipster.lite.sample.common.domain.Generated;

@Generated
@Controller
class RedirectionResource {

  @GetMapping(value = "/{path:[^\\.]*}")
  public String redirectApi() {
    return "forward:/";
  }
}
