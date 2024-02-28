package tech.jhipster.lite.sample.shared.error.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class NotBeforeTimeExceptionTest {

  private static final String FIELD = "myField";
  private static final Instant VALUE = Instant.parse("2024-02-21T21:00:00Z");

  @Test
  void shouldGetNotBeforeExceptionInformation() {
    NotBeforeTimeException exception = NotBeforeTimeException.notBefore().value(VALUE).field(FIELD).other(Instant.ofEpochSecond(1337));

    assertDefaultInformation(exception);
    assertThat(exception.getMessage()).isEqualTo("Time 2024-02-21T21:00:00Z in \"myField\" must be before 1970-01-01T00:22:17Z but wasn't");
  }

  @Test
  void shouldGetNotStrictlyAfterExceptionInformation() {
    NotBeforeTimeException exception = NotBeforeTimeException.strictlyNotBefore()
      .value(VALUE)
      .field(FIELD)
      .other(Instant.ofEpochSecond(1337));

    assertDefaultInformation(exception);
    assertThat(exception.getMessage()).isEqualTo(
      "Time 2024-02-21T21:00:00Z in \"myField\" must be strictly before 1970-01-01T00:22:17Z but wasn't"
    );
  }

  private void assertDefaultInformation(NotBeforeTimeException exception) {
    assertThat(exception.type()).isEqualTo(AssertionErrorType.NOT_BEFORE_TIME);
    assertThat(exception.field()).isEqualTo(FIELD);
    assertThat(exception.parameters()).isEmpty();
  }
}
