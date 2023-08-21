package tech.jhipster.lite.sample.shared.memoizer.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class MemoizersTest {

  @Test
  void shouldGetFunctionResult() {
    Function<Double, Double> memoizer = Memoizers.of(d -> d * d);

    assertThat(memoizer.apply(2D)).isEqualTo(4D);
  }

  @Test
  void shouldMemoizeFunctionResult() {
    AtomicInteger result = new AtomicInteger();

    Function<Object, Integer> memoizer = Memoizers.of(d -> result.incrementAndGet());

    assertThat(memoizer.apply(1)).isEqualTo(memoizer.apply(1));
    assertThat(memoizer.apply(1)).isNotEqualTo(memoizer.apply(2));
  }

  @Test
  void shouldMemoizeNullResult() {
    NullFactory factory = new NullFactory();
    Function<Object, String> memoizer = Memoizers.of(factory);

    memoizer.apply(1);
    memoizer.apply(1);

    assertThat(factory.callsCount()).isEqualTo(1);
    assertThat(memoizer.apply(1)).isNull();
  }

  @Test
  void shouldMemoizeSupplier() {
    Supplier<String> supplier = Memoizers.of(() -> "Pouet");

    assertThat(supplier.get()).isEqualTo("Pouet");
  }

  private static class NullFactory implements Function<Object, String> {

    private final AtomicInteger callsCount = new AtomicInteger();

    public int callsCount() {
      return callsCount.get();
    }

    @Override
    public String apply(Object input) {
      callsCount.incrementAndGet();

      return null;
    }
  }
}
