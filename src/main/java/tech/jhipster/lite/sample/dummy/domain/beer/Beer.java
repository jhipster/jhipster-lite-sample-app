package tech.jhipster.lite.sample.dummy.domain.beer;

import java.math.BigDecimal;
import tech.jhipster.lite.sample.dummy.domain.Amount;
import tech.jhipster.lite.sample.dummy.domain.BeerId;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public final class Beer {

  private final BeerId id;
  private final BeerName name;
  private final Amount unitPrice;

  private BeerSellingState sellingState;

  private Beer(BeerBuilder builder) {
    assertMandatoryFields(builder);

    id = builder.id;
    name = builder.name;
    unitPrice = builder.unitPrice;
    sellingState = builder.sellingState;
  }

  private void assertMandatoryFields(BeerBuilder builder) {
    Assert.notNull("id", builder.id);
    Assert.notNull("name", builder.name);
    Assert.notNull("unitPrice", builder.unitPrice);
    Assert.notNull("sellingState", builder.sellingState);
  }

  public static BeerIdBuilder builder() {
    return new BeerBuilder();
  }

  public BeerId id() {
    return id;
  }

  public BeerName name() {
    return name;
  }

  public Amount unitPrice() {
    return unitPrice;
  }

  public void removeFromSell() {
    sellingState = BeerSellingState.NOT_SOLD;
  }

  public BeerSellingState sellingState() {
    return sellingState;
  }

  private static class BeerBuilder
    implements BeerIdBuilder, BeerNameBuilder, BeerUnitPriceBuilder, BeerSellStateBuilder, BeerOptionalBuilder {

    private BeerId id;
    private BeerName name;
    private Amount unitPrice;
    private BeerSellingState sellingState;

    @Override
    public BeerNameBuilder id(BeerId id) {
      this.id = id;

      return this;
    }

    @Override
    public BeerUnitPriceBuilder name(BeerName name) {
      this.name = name;

      return this;
    }

    @Override
    public BeerSellStateBuilder unitPrice(Amount unitPrice) {
      this.unitPrice = unitPrice;

      return this;
    }

    @Override
    public BeerOptionalBuilder sellingState(BeerSellingState sellingState) {
      this.sellingState = sellingState;

      return this;
    }

    @Override
    public Beer build() {
      return new Beer(this);
    }
  }

  public interface BeerIdBuilder {
    BeerNameBuilder id(BeerId id);
  }

  public interface BeerNameBuilder {
    BeerUnitPriceBuilder name(BeerName name);

    default BeerUnitPriceBuilder name(String name) {
      return name(new BeerName(name));
    }
  }

  public interface BeerUnitPriceBuilder {
    BeerSellStateBuilder unitPrice(Amount unitPrice);

    default BeerSellStateBuilder unitPrice(BigDecimal unitPrice) {
      return unitPrice(new Amount(unitPrice));
    }
  }

  public interface BeerSellStateBuilder {
    BeerOptionalBuilder sellingState(BeerSellingState sellingState);
  }

  public interface BeerOptionalBuilder {
    Beer build();
  }
}
