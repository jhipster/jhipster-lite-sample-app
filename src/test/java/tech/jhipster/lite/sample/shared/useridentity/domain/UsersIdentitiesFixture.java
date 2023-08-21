package tech.jhipster.lite.sample.shared.useridentity.domain;

import tech.jhipster.lite.sample.shared.authentication.domain.Username;

public final class UsersIdentitiesFixture {

  private UsersIdentitiesFixture() {}

  public static Username username() {
    return new Username("user");
  }

  public static Name name() {
    return new Name(firstname(), lastname());
  }

  public static final Firstname firstname() {
    return new Firstname("Paul");
  }

  public static final Lastname lastname() {
    return new Lastname("DUPOND");
  }

  public static Email email() {
    return new Email("email@company.fr");
  }
}
