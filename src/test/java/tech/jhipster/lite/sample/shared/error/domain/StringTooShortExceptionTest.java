package tech.jhipster.lite.sample.shared.error.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class StringTooShortExceptionTest {

  @Test
  void shouldGetExceptionInformation() {
    StringTooShortException exception = StringTooShortException.builder().field("myField").minLength(10).value("value").build();

    assertThat(exception.type()).isEqualTo(AssertionErrorType.STRING_TOO_SHORT);
    assertThat(exception.field()).isEqualTo("myField");
    assertThat(exception.parameters()).containsOnly(entry("minLength", "10"), entry("currentLength", "5"));
    assertThat(exception.getMessage()).isEqualTo("The value in field \"myField\" must be at least 10 long but was only 5");
  }
}
