package tech.jhipster.lite.sample.shared.enumeration.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.error.domain.MissingMandatoryValueException;

@UnitTest
class EnumsTest {

  @Test
  void shouldNotMapWithoutTo() {
    assertThatThrownBy(() -> Enums.map(Primary.ONE, null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("to");
  }

  @Test
  void shouldMapToNullFromNullFrom() {
    assertThat(Enums.map(null, Primary.class)).isNull();
  }

  @Test
  void shouldMapFromPrimaryToDomain() {
    assertThat(Enums.map(Primary.ONE, Domain.class)).isEqualTo(Domain.ONE);
  }

  @Test
  void shouldMapFromDomainToPrimary() {
    assertThat(Enums.map(Domain.ONE, Primary.class)).isEqualTo(Primary.ONE);
  }

  @Test
  void shouldNotMapWithMoreValueInSourceThanDestination() {
    assertThatThrownBy(() -> Enums.map(Other.ONE, Primary.class)).isExactlyInstanceOf(UnmappableEnumException.class);
  }

  @Test
  void shouldMapWithMoreValueInDestinationThanSource() {
    assertThat(Enums.map(Primary.ONE, Other.class)).isEqualTo(Other.ONE);
  }

  private enum Primary {
    ONE,
  }

  private enum Domain {
    ONE,
  }

  private enum Other {
    ONE,
    TWO,
  }
}
