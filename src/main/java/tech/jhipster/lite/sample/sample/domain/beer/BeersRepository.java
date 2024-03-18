package tech.jhipster.lite.sample.sample.domain.beer;

import java.util.Optional;
import tech.jhipster.lite.sample.sample.domain.BeerId;

public interface BeersRepository {
  void save(Beer beer);

  Beers catalog();

  Optional<Beer> get(BeerId beer);
}
