package tech.jhipster.lite.sample.dummy.infrastructure.secondary;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;
import tech.jhipster.lite.sample.dummy.domain.BeerId;
import tech.jhipster.lite.sample.dummy.domain.beer.Beer;
import tech.jhipster.lite.sample.dummy.domain.beer.BeerSellingState;
import tech.jhipster.lite.sample.dummy.domain.beer.Beers;
import tech.jhipster.lite.sample.dummy.domain.beer.BeersRepository;
import tech.jhipster.lite.sample.error.domain.Assert;

@Repository
class InMemoryBeersRepository implements BeersRepository {

  private final Map<BeerId, Beer> beers = new ConcurrentHashMap<>();

  @Override
  public void save(Beer beer) {
    Assert.notNull("beer", beer);

    beers.put(beer.id(), beer);
  }

  @Override
  public Beers catalog() {
    return new Beers(beers.values().stream().filter(soldBeer()).toList());
  }

  private Predicate<Beer> soldBeer() {
    return beer -> beer.sellingState() == BeerSellingState.SOLD;
  }

  @Override
  public Optional<Beer> get(BeerId beer) {
    Assert.notNull("beer", beer);

    return Optional.ofNullable(beers.get(beer));
  }

  public void clear() {
    beers.clear();
  }
}
