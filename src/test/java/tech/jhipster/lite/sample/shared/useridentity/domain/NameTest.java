package tech.jhipster.lite.sample.shared.useridentity.domain;

import static tech.jhipster.lite.sample.shared.useridentity.domain.UsersIdentitiesFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.error.domain.MissingMandatoryValueException;

@UnitTest
class NameTest {

  @Test
  void shouldNotBuildWithoutFirstname() {
    assertThatThrownBy(() -> new Name(null, lastname()))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("firstname");
  }

  @Test
  void shouldNotBuildWithoutLastname() {
    assertThatThrownBy(() -> new Name(firstname(), null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("lastname");
  }

  @Test
  void shouldGetFullname() {
    Name name = new Name("paul", "Dupond");

    assertThat(name.get()).isEqualTo("Paul DUPOND");
    assertThat(name.firstname()).isEqualTo(new Firstname("paul"));
    assertThat(name.lastname()).isEqualTo(new Lastname("Dupond"));
  }

  @Test
  void shouldSortNames() {
    List<Name> names = Stream
      .of(null, new Name("paul", "Dupond"), new Name("jean", "Dupont"), new Name("jean", "Dupond"))
      .sorted()
      .toList();

    assertThat(names).containsExactly(new Name("jean", "Dupond"), new Name("jean", "Dupont"), new Name("paul", "Dupond"), null);
  }
}
