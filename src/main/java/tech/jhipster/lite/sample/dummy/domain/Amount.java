package tech.jhipster.lite.sample.dummy.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import tech.jhipster.lite.sample.error.domain.Assert;

public record Amount(BigDecimal amount) {
  public static final Amount ZERO = new Amount(BigDecimal.ZERO);

  public Amount(BigDecimal amount) {
    Assert.field("amount", amount).notNull().min(BigDecimal.ZERO);

    this.amount = amount.setScale(2, RoundingMode.HALF_UP);
  }

  public BigDecimal get() {
    return amount();
  }

  public Amount times(int value) {
    return new Amount(amount().multiply(new BigDecimal(value)));
  }

  public Amount add(Amount other) {
    Assert.notNull("other", other);

    return new Amount(amount().add(other.amount()));
  }
}
