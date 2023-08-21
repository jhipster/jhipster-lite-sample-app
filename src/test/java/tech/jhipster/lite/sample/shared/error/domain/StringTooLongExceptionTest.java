package tech.jhipster.lite.sample.shared.error.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class StringTooLongExceptionTest {

  @Test
  void shouldGetExceptionInformation() {
    StringTooLongException exception = StringTooLongException.builder().field("myField").maxLength(2).value("value").build();

    assertThat(exception.type()).isEqualTo(AssertionErrorType.STRING_TOO_LONG);
    assertThat(exception.field()).isEqualTo("myField");
    assertThat(exception.parameters()).containsOnly(entry("maxLength", "2"), entry("currentLength", "5"));
    assertThat(exception.getMessage()).isEqualTo("The value in field \"myField\" must be at most 2 long but was 5");
  }
}
