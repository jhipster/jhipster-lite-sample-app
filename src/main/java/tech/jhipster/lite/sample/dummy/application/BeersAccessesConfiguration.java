package tech.jhipster.lite.sample.dummy.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;
import tech.jhipster.lite.sample.shared.kipe.domain.RolesAccesses;

@Configuration
class BeersAccessesConfiguration {

  @Bean
  RolesAccesses beersAccesses() {
    return RolesAccesses.builder()
      .role(Role.ADMIN)
      .allAuthorized("create", BeerResource.BEERS)
      .allAuthorized("remove", BeerResource.BEERS)
      .and()
      .build();
  }
}
