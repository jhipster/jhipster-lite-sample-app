package tech.jhipster.lite.sample.useridentity.domain;

import java.util.Comparator;
import tech.jhipster.lite.sample.error.domain.Assert;

public record Name(Firstname firstname, Lastname lastname) implements Comparable<Name> {
  private static final Comparator<Name> COMPARATOR = Comparator.comparing(Name::firstname).thenComparing(Name::lastname);

  public Name(String firstname, String lastname) {
    this(new Firstname(firstname), new Lastname(lastname));
  }

  public Name {
    Assert.notNull("firstname", firstname);
    Assert.notNull("lastname", lastname);
  }

  public String get() {
    return firstname.get() + " " + lastname.get();
  }

  @Override
  public int compareTo(Name other) {
    if (other == null) {
      return -1;
    }

    return COMPARATOR.compare(this, other);
  }
}
