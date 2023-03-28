package tech.jhipster.lite.sample.authentication.domain;

import java.util.Set;
import java.util.stream.Stream;
import tech.jhipster.lite.sample.common.domain.LitesampleCollections;
import tech.jhipster.lite.sample.error.domain.Assert;

public record Roles(Set<Role> roles) {
  public static final Roles EMPTY = new Roles(null);

  public Roles(Set<Role> roles) {
    this.roles = LitesampleCollections.immutable(roles);
  }

  public boolean hasRole() {
    return !roles.isEmpty();
  }

  public boolean hasRole(Role role) {
    Assert.notNull("role", role);

    return roles.contains(role);
  }

  public Stream<Role> stream() {
    return get().stream();
  }

  public Set<Role> get() {
    return roles();
  }
}
