package tech.jhipster.lite.sample.common.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import tech.jhipster.lite.sample.error.domain.Assert;

public final class Enums {

  private static final EnumMappings MAPPINGS = new EnumMappings();

  private Enums() {}

  public static <From extends Enum<From>, To extends Enum<To>> To map(Enum<From> from, Class<To> to) {
    Assert.notNull("to", to);

    if (from == null) {
      return null;
    }

    return MAPPINGS.get(from, to);
  }

  private static final class EnumMappings {

    private final Map<CacheKey<?, ?>, Map<Enum<?>, Enum<?>>> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    private <From extends Enum<From>, To extends Enum<To>> To get(Enum<From> from, Class<To> to) {
      return (To) cache.computeIfAbsent(new CacheKey<>(from.getClass(), to), buildCache(from)).get(from);
    }

    @SuppressWarnings("unchecked")
    private <To extends Enum<To>> Function<CacheKey<?, ?>, Map<Enum<?>, Enum<?>>> buildCache(Enum<?> from) {
      return key -> {
        try {
          return Arrays
            .stream(key.from().getEnumConstants())
            .collect(Collectors.toMap(Function.identity(), source -> Enum.valueOf(key.to(), source.name())));
        } catch (IllegalArgumentException e) {
          throw new UnmappableEnumException(from.getClass(), key.to());
        }
      };
    }

    private record CacheKey<From extends Enum<From>, To extends Enum<To>>(Class<From> from, Class<To> to) {}
  }
}
