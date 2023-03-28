package tech.jhipster.lite.sample.kipe.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tech.jhipster.lite.sample.authentication.domain.Role;
import tech.jhipster.lite.sample.authentication.domain.Roles;
import tech.jhipster.lite.sample.error.domain.Assert;
import tech.jhipster.lite.sample.kipe.domain.Accesses.RoleAccessesBuilder;

public final class RolesAccesses {

  public static final RolesAccesses EMPTY = builder().build();

  private final Map<Role, Accesses> roles;

  private RolesAccesses(RolesAccessesBuilder builder) {
    roles = builder.roles.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
  }

  private RolesAccesses(Map<Role, Accesses> roles) {
    this.roles = roles;
  }

  public static RolesAccessesBuilder builder() {
    return new RolesAccessesBuilder();
  }

  public boolean allAuthorized(Roles userRoles, Action action, Resource resource) {
    Assert.notNull("userRoles", userRoles);
    Assert.notNull("action", action);
    Assert.notNull("resource", resource);

    return userRoles.stream().anyMatch(allAuthorized(action, resource));
  }

  private Predicate<Role> allAuthorized(Action action, Resource resource) {
    return role -> {
      Accesses accesses = roles.get(role);

      if (accesses == null) {
        return false;
      }

      return accesses.allAuthorized(action, resource);
    };
  }

  public boolean specificAuthorized(Roles userRoles, Action action, Resource resource) {
    Assert.notNull("userRoles", userRoles);
    Assert.notNull("action", action);
    Assert.notNull("resource", resource);

    return userRoles.stream().anyMatch(specificAuthorized(action, resource));
  }

  private Predicate<Role> specificAuthorized(Action action, Resource resource) {
    return role -> {
      Accesses accesses = roles.get(role);

      if (accesses == null) {
        return false;
      }

      return accesses.specificAuthorized(action, resource);
    };
  }

  public RolesAccesses merge(RolesAccesses other) {
    Assert.notNull("other", other);

    Map<Role, Accesses> mergedRoles = Stream
      .concat(roles.entrySet().stream(), other.roles.entrySet().stream())
      .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, Accesses::merge));

    return new RolesAccesses(mergedRoles);
  }

  public static class RolesAccessesBuilder {

    private final Map<Role, RoleAccessesBuilder> roles = new ConcurrentHashMap<>();

    private RolesAccessesBuilder() {}

    public RoleAccessesBuilder role(Role role) {
      Assert.notNull("role", role);

      return roles.computeIfAbsent(role, key -> Accesses.builder(this));
    }

    public RolesAccesses build() {
      return new RolesAccesses(this);
    }
  }
}
