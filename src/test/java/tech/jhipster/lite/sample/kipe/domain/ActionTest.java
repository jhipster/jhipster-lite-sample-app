package tech.jhipster.lite.sample.kipe.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class ActionTest {

  @Test
  void shouldGetActionAsToString() {
    assertThat(new Action("act")).hasToString("act");
  }
}
