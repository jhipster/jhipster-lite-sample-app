package tech.jhipster.lite.sample.kipe.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tech.jhipster.lite.sample.error.domain.Assert;
import tech.jhipster.lite.sample.kipe.domain.RolesAccesses.RolesAccessesBuilder;

public class Accesses {

  private final Map<Action, Map<String, Boolean>> actionsAccesses;

  private Accesses(RoleAccessesBuilder builder) {
    actionsAccesses = buildActionsAccesses(builder);
  }

  private Accesses(Map<Action, Map<String, Boolean>> accesses) {
    this.actionsAccesses = accesses;
  }

  private Map<Action, Map<String, Boolean>> buildActionsAccesses(RoleAccessesBuilder builder) {
    return builder.accesses.stream().collect(Collectors.groupingBy(Access::action)).entrySet().stream().collect(toAccesses());
  }

  private Collector<Entry<Action, List<Access>>, ?, Map<Action, Map<String, Boolean>>> toAccesses() {
    return Collectors.toUnmodifiableMap(
      Map.Entry::getKey,
      entry -> entry.getValue().stream().collect(Collectors.toUnmodifiableMap(Access::resource, Access::global))
    );
  }

  static RoleAccessesBuilder builder(RolesAccessesBuilder source) {
    return new RoleAccessesBuilder(source);
  }

  boolean allAuthorized(Action action, Resource resource) {
    Map<String, Boolean> actionAccesses = actionsAccesses.get(action);

    if (actionAccesses == null) {
      return false;
    }

    Boolean allAuthorized = actionAccesses.get(resource.key());
    return Boolean.TRUE.equals(allAuthorized);
  }

  boolean specificAuthorized(Action action, Resource resource) {
    Map<String, Boolean> actionAccesses = actionsAccesses.get(action);

    if (actionAccesses == null) {
      return false;
    }

    return actionAccesses.containsKey(resource.key());
  }

  Accesses merge(Accesses other) {
    Map<Action, Map<String, Boolean>> mergedAccesses = Stream
      .concat(actionsAccesses.entrySet().stream(), other.actionsAccesses.entrySet().stream())
      .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, mergeResourceAccesses()));

    return new Accesses(mergedAccesses);
  }

  private BinaryOperator<Map<String, Boolean>> mergeResourceAccesses() {
    return (first, second) ->
      Stream
        .concat(first.entrySet().stream(), second.entrySet().stream())
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, mergeAccessesScopes()));
  }

  private BinaryOperator<Boolean> mergeAccessesScopes() {
    return (first, second) -> {
      if (Boolean.TRUE.equals(first)) {
        return first;
      }

      return second;
    };
  }

  public static class RoleAccessesBuilder {

    private final RolesAccessesBuilder source;
    private final Collection<Access> accesses = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private RoleAccessesBuilder(RolesAccessesBuilder source) {
      Assert.notNull("source", source);

      this.source = source;
    }

    public RoleAccessesBuilder allAuthorized(String action, Resource resource) {
      accesses.add(Access.allAuthorized(new Action(action), resource));

      return this;
    }

    public RoleAccessesBuilder specificAuthorized(String action, Resource resource) {
      accesses.add(Access.specificAuthorized(new Action(action), resource));

      return this;
    }

    public RolesAccessesBuilder and() {
      return source;
    }

    public Accesses build() {
      return new Accesses(this);
    }
  }

  private record Access(Action action, String resource, boolean global) {
    public static Access allAuthorized(Action action, Resource resource) {
      Assert.notNull("resource", resource);

      return new Access(action, resource.key(), true);
    }

    public static Access specificAuthorized(Action action, Resource resource) {
      Assert.notNull("resource", resource);

      return new Access(action, resource.key(), false);
    }
  }
}
