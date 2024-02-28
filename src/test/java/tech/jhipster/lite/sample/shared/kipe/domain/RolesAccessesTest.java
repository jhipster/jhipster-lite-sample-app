package tech.jhipster.lite.sample.shared.kipe.domain;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.sample.shared.kipe.domain.RolesAccessesFixture.*;

import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.shared.authentication.domain.Role;
import tech.jhipster.lite.sample.shared.authentication.domain.Roles;

@UnitTest
class RolesAccessesTest {

  @Nested
  class AllAuthorizedTest {

    @Test
    void shouldNotBeAllAuthorizedOnEmptyRolesAccesses() {
      assertThat(RolesAccesses.EMPTY.allAuthorized(admin(), action("read"), TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAllAuthorizedForUnknownAction() {
      assertThat(rolesAccesses().allAuthorized(admin(), action("unknown"), TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeAllAuthorizedForSpecificAction() {
      assertThat(rolesAccesses().allAuthorized(user(), action("read"), TestResource.USERS)).isFalse();
    }

    @Test
    void shouldBeAllAuthorizedForAllAuthorizedAction() {
      assertThat(rolesAccesses().allAuthorized(admin(), action("read"), TestResource.USERS)).isTrue();
    }
  }

  @Nested
  class SpecificAuthorizedTest {

    @Test
    void shouldNotBeSpecificAuthorizedOnEmptyRolesAccesses() {
      assertThat(RolesAccesses.EMPTY.specificAuthorized(user(), action("read"), TestResource.USERS)).isFalse();
    }

    @Test
    void shouldNotBeSpecificAuthorizedForUnknownAction() {
      assertThat(rolesAccesses().specificAuthorized(user(), action("unknown"), TestResource.USERS)).isFalse();
    }

    @Test
    void shouldBeSpecificAuthorizedForSpecificAuthorizedAction() {
      assertThat(rolesAccesses().specificAuthorized(user(), action("read"), TestResource.USERS)).isTrue();
    }

    @Test
    void shouldBeSpecificAuthorizedForAllAuthorizedAction() {
      assertThat(rolesAccesses().specificAuthorized(admin(), action("read"), TestResource.USERS)).isTrue();
    }
  }

  @Test
  void shouldMergeAccesses() {
    RolesAccesses initial = RolesAccesses.builder()
      .role(Role.USER)
      .allAuthorized("read", TestResource.USERS)
      .specificAuthorized("update", TestResource.USERS)
      .and()
      .build();

    RolesAccesses accesses = initial.merge(rolesAccesses());

    assertThat(accesses.allAuthorized(user(), action("read"), TestResource.USERS)).isTrue();
    assertThat(accesses.specificAuthorized(user(), action("update"), TestResource.USERS)).isTrue();
    assertThat(accesses.specificAuthorized(admin(), action("read"), TestResource.DATA)).isTrue();
  }

  private static Roles admin() {
    return new Roles(Set.of(Role.ADMIN));
  }

  private static Roles user() {
    return new Roles(Set.of(Role.USER));
  }

  private static Action action(String action) {
    return new Action(action);
  }
}
