package tech.jhipster.lite.sample.shared.error.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class NotBeforeTimeExceptionTest {

  private static final String FIELD = "myField";

  @Test
  void shouldGetNotBeforeExceptionInformation() {
    NotBeforeTimeException exception = NotBeforeTimeException
      .field(FIELD, Instant.ofEpochSecond(1337))
      .notBefore(Instant.ofEpochSecond(1338));

    assertDefaultInformation(exception);
    assertThat(exception.getMessage())
      .isEqualTo("Time in \"myField\" having value : 1970-01-01T00:22:17Z must be before 1970-01-01T00:22:18Z but wasn't");
  }

  @Test
  void shouldGetStrictlyNotBeforeExceptionInformation() {
    NotBeforeTimeException exception = NotBeforeTimeException
      .field(FIELD, Instant.ofEpochSecond(1337))
      .strictlyNotBefore(Instant.ofEpochSecond(1337));

    assertDefaultInformation(exception);
    assertThat(exception.getMessage())
      .isEqualTo("Time in \"myField\" having value : 1970-01-01T00:22:17Z must be strictly before 1970-01-01T00:22:17Z but wasn't");
  }

  private void assertDefaultInformation(NotBeforeTimeException exception) {
    assertThat(exception.type()).isEqualTo(AssertionErrorType.NOT_BEFORE_TIME);
    assertThat(exception.field()).isEqualTo(FIELD);
    assertThat(exception.parameters()).isEmpty();
  }
}
