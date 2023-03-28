package tech.jhipster.lite.sample.dummy.application;

import org.springframework.stereotype.Component;
import tech.jhipster.lite.sample.dummy.domain.BeerId;
import tech.jhipster.lite.sample.kipe.application.AccessChecker;
import tech.jhipster.lite.sample.kipe.application.AccessContext;
import tech.jhipster.lite.sample.kipe.application.LitesampleAuthorizations;

@Component
class BeerIdAccessChecker implements AccessChecker<BeerId> {

  private final LitesampleAuthorizations authorizations;

  public BeerIdAccessChecker(LitesampleAuthorizations authorizations) {
    this.authorizations = authorizations;
  }

  @Override
  public boolean can(AccessContext<BeerId> access) {
    return authorizations.allAuthorized(access.authentication(), access.action(), BeerResource.BEERS);
  }
}
