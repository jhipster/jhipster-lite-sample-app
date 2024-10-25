package tech.jhipster.lite.sample.shared.useridentity.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.error.domain.MissingMandatoryValueException;
import tech.jhipster.lite.sample.shared.error.domain.StringTooLongException;

@UnitTest
class FirstnameTest {

  @Test
  void shouldNotBuildWithoutFirstname() {
    assertThatThrownBy(() -> new Firstname(null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("firstname");
  }

  @Test
  void shouldNotBuildWithBlankFirstname() {
    assertThatThrownBy(() -> new Firstname(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("firstname");
  }

  @Test
  void shouldNotBuildWithTooLongFirstname() {
    assertThatThrownBy(() -> new Firstname("a".repeat(151)))
      .isExactlyInstanceOf(StringTooLongException.class)
      .hasMessageContaining("firstname");
  }

  @Test
  void shouldCapitalizeFirstname() {
    Firstname firstname = new Firstname("jean");

    assertThat(firstname.get()).isEqualTo("Jean");
  }

  @Test
  void shouldCapitalizeComposedFirstname() {
    Firstname firstname = new Firstname("jean-PAUL jÉrémie");

    assertThat(firstname.get()).isEqualTo("Jean-Paul Jérémie");
  }

  @Test
  void shouldSortFirstnames() {
    List<Firstname> firstnames = Stream.of(null, new Firstname("paul"), new Firstname("jean")).sorted().toList();

    assertThat(firstnames).containsExactly(new Firstname("jean"), new Firstname("paul"), null);
  }
}
