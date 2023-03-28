package tech.jhipster.lite.sample.dummy.infrastructure.secondary;

import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class InMemoryBeersReseter {

  @Autowired
  private InMemoryBeersRepository beers;

  @Before
  public void resetBeersCatalog() {
    beers.clear();
  }
}
