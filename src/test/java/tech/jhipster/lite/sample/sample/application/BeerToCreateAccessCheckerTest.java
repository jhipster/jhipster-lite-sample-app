package tech.jhipster.lite.sample.sample.application;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.sample.sample.domain.beer.BeersFixture.*;
import static tech.jhipster.lite.sample.shared.kipe.application.TestAuthentications.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.kipe.application.AccessContext;
import tech.jhipster.lite.sample.shared.kipe.application.LitesampleAuthorizations;

@UnitTest
class BeerToCreateAccessCheckerTest {

  private static final BeerToCreateAccessChecker checker = new BeerToCreateAccessChecker(
    new LitesampleAuthorizations(List.of(new BeersAccessesConfiguration().beersAccesses()))
  );

  @Test
  void shouldNotAuthorizedUnauthorizedAction() {
    assertThat(checker.can(AccessContext.of(admin(), "unauthorized", beerToCreate()))).isFalse();
  }

  @Test
  void shouldAuthorizedAuthorizedAction() {
    assertThat(checker.can(AccessContext.of(admin(), "create", beerToCreate()))).isTrue();
  }
}
