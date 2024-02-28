package tech.jhipster.lite.sample.shared.error.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class NotAfterTimeExceptionTest {

  private static final String FIELD = "myField";
  private static final Instant VALUE = Instant.ofEpochSecond(1337);
  private static final Instant OTHER = Instant.parse("2024-02-21T21:00:00Z");

  @Test
  void shouldGetNotAfterExceptionInformation() {
    NotAfterTimeException exception = NotAfterTimeException.notAfter().value(VALUE).field(FIELD).other(OTHER);

    assertDefaultInformation(exception);
    assertThat(exception.getMessage()).isEqualTo("Time 1970-01-01T00:22:17Z in \"myField\" must be after 2024-02-21T21:00:00Z but wasn't");
  }

  @Test
  void shouldGetNotStrictlyAfterExceptionInformation() {
    NotAfterTimeException exception = NotAfterTimeException.strictlyNotAfter().value(VALUE).field(FIELD).other(OTHER);

    assertDefaultInformation(exception);
    assertThat(exception.getMessage()).isEqualTo(
      "Time 1970-01-01T00:22:17Z in \"myField\" must be strictly after 2024-02-21T21:00:00Z but wasn't"
    );
  }

  private void assertDefaultInformation(NotAfterTimeException exception) {
    assertThat(exception.type()).isEqualTo(AssertionErrorType.NOT_AFTER_TIME);
    assertThat(exception.field()).isEqualTo(FIELD);
    assertThat(exception.parameters()).isEmpty();
  }
}
