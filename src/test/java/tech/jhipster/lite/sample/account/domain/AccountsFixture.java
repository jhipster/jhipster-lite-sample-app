package tech.jhipster.lite.sample.account.domain;

import static tech.jhipster.lite.sample.useridentity.domain.UsersIdentitiesFixture.*;

import java.util.List;
import tech.jhipster.lite.sample.authentication.domain.Role;

public final class AccountsFixture {

  private AccountsFixture() {}

  public static Account account() {
    return Account
      .builder()
      .username(username())
      .firstname(firstname())
      .lastname(lastname())
      .email(email())
      .roles(List.of(Role.ADMIN.key()))
      .build();
  }
}
