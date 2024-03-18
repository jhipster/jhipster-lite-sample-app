package tech.jhipster.lite.sample.sample.application;

import org.springframework.stereotype.Component;
import tech.jhipster.lite.sample.sample.domain.beer.BeerToCreate;
import tech.jhipster.lite.sample.shared.kipe.application.AccessChecker;
import tech.jhipster.lite.sample.shared.kipe.application.AccessContext;
import tech.jhipster.lite.sample.shared.kipe.application.LitesampleAuthorizations;

@Component
class BeerToCreateAccessChecker implements AccessChecker<BeerToCreate> {

  private final LitesampleAuthorizations authorizations;

  public BeerToCreateAccessChecker(LitesampleAuthorizations authorizations) {
    this.authorizations = authorizations;
  }

  @Override
  public boolean can(AccessContext<BeerToCreate> access) {
    return authorizations.allAuthorized(access.authentication(), access.action(), BeerResource.BEERS);
  }
}
