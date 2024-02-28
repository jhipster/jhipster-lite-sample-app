package tech.jhipster.lite.sample.dummy.domain;

import java.math.BigDecimal;
import java.util.UUID;

public final class BeersIdentityFixture {

  private BeersIdentityFixture() {}

  public static BeerId cloackOfFeathersId() {
    return new BeerId(UUID.fromString("5ea9bbb1-3a55-4701-9006-3bbd2878f241"));
  }

  public static Amount cloakOfFeatherUnitPrice() {
    return new Amount(new BigDecimal("8.53"));
  }

  public static BeerId anteMeridiemId() {
    return new BeerId(UUID.fromString("b38c2933-1a61-4bac-995d-8e535f4f64a4"));
  }

  public static Amount anteMeridiemUnitPrice() {
    return new Amount(new BigDecimal("5.46"));
  }
}
