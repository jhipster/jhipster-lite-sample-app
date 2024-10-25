package tech.jhipster.lite.sample.shared.useridentity.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.error.domain.MissingMandatoryValueException;
import tech.jhipster.lite.sample.shared.error.domain.StringTooLongException;

@UnitTest
class EmailTest {

  @Test
  void shouldNotBuildWithoutEmail() {
    assertThatThrownBy(() -> new Email(null)).isExactlyInstanceOf(MissingMandatoryValueException.class).hasMessageContaining("email");
  }

  @Test
  void shouldNotBuildWithBlankEmail() {
    assertThatThrownBy(() -> new Email(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class).hasMessageContaining("email");
  }

  @Test
  void shouldNotBuildWithTooLongEmail() {
    assertThatThrownBy(() -> new Email("a".repeat(256))).isExactlyInstanceOf(StringTooLongException.class).hasMessageContaining("email");
  }

  @Test
  void shouldBeEmptyWithoutEmail() {
    assertThat(Email.of(null)).isEmpty();
  }

  @Test
  void shouldBeEmptyWithBlankEmail() {
    assertThat(Email.of(" ")).isEmpty();
  }

  @Test
  void shouldGetEmail() {
    Email email = new Email("mail@company.fr");

    assertThat(email.get()).isEqualTo("mail@company.fr");
  }
}
