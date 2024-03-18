package tech.jhipster.lite.sample.sample.domain.order;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import tech.jhipster.lite.sample.sample.domain.Amount;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public final class BeerOrder {

  private final Collection<BeerOrderLine> lines;
  private final Amount total;

  private BeerOrder(BeerOrderBuilder builder) {
    lines = buildLines(builder);
    total = buildTotal();

    Assert.notEmpty("lines", lines);
  }

  private List<BeerOrderLine> buildLines(BeerOrderBuilder builder) {
    return builder.lines.entrySet().stream().map(toBeerOrderLine()).toList();
  }

  private Function<Entry<OrderedBeer, Integer>, BeerOrderLine> toBeerOrderLine() {
    return entry -> new BeerOrderLine(entry.getKey(), entry.getValue());
  }

  private Amount buildTotal() {
    return lines.stream().map(BeerOrderLine::amount).reduce(Amount.ZERO, Amount::add);
  }

  public static BeerOrderBuilder builder() {
    return new BeerOrderBuilder();
  }

  public Collection<BeerOrderLine> lines() {
    return lines;
  }

  public Amount total() {
    return total;
  }

  public static class BeerOrderBuilder {

    private final Map<OrderedBeer, Integer> lines = new HashMap<>();

    public BeerOrderBuilder add(OrderedBeer beer) {
      return add(beer, 1);
    }

    public BeerOrderBuilder add(OrderedBeer beer, int quantity) {
      Assert.field("quantity", quantity).min(1);

      lines.compute(beer, addBeers(quantity));

      return this;
    }

    private BiFunction<OrderedBeer, Integer, Integer> addBeers(int quantity) {
      return (currentBeer, currentQuantity) -> {
        if (currentQuantity == null) {
          return quantity;
        }

        return currentQuantity + quantity;
      };
    }

    public BeerOrder build() {
      return new BeerOrder(this);
    }
  }
}
