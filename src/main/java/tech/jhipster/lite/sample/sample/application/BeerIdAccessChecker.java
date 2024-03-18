package tech.jhipster.lite.sample.sample.application;

import org.springframework.stereotype.Component;
import tech.jhipster.lite.sample.sample.domain.BeerId;
import tech.jhipster.lite.sample.shared.kipe.application.AccessChecker;
import tech.jhipster.lite.sample.shared.kipe.application.AccessContext;
import tech.jhipster.lite.sample.shared.kipe.application.LitesampleAuthorizations;

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
