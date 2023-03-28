package tech.jhipster.lite.sample.dummy.domain.beer;

import java.util.Optional;
import tech.jhipster.lite.sample.dummy.domain.BeerId;

public interface BeersRepository {
  void save(Beer beer);

  Beers catalog();

  Optional<Beer> get(BeerId beer);
}
