package tech.jhipster.lite.sample.sample.domain.beer;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record Beers(Collection<Beer> beers) {
  private static final Comparator<Beer> BEER_NAME_COMPARATOR = Comparator.comparing(beer -> beer.name().get());

  public Beers(Collection<Beer> beers) {
    Assert.field("beers", beers).notNull().noNullElement();

    this.beers = beers.stream().sorted(BEER_NAME_COMPARATOR).toList();
  }

  public Collection<Beer> get() {
    return beers();
  }

  public Stream<Beer> stream() {
    return beers().stream();
  }
}
