package tech.jhipster.lite.sample.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class LitesampleCollectionsTest {

  @Nested
  @DisplayName("Collections")
  class LitesampleCollectionsCollectionsTest {

    @Test
    void shouldGetEmptyImmutableCollectionFromNullCollection() {
      Collection<Object> input = null;
      Collection<Object> collection = LitesampleCollections.immutable(input);

      assertThat(collection).isEmpty();
      assertThatThrownBy(collection::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableCollectionFromMutableCollection() {
      Collection<String> input = new ArrayList<>();
      input.add("value");
      Collection<String> collection = LitesampleCollections.immutable(input);

      assertThat(collection).containsExactly("value");
      assertThatThrownBy(collection::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Nested
  @DisplayName("Set")
  class LitesampleCollectionsSetTest {

    @Test
    void shouldGetEmptyImmutableCollectionFromNullCollection() {
      Set<Object> input = null;
      Set<Object> set = LitesampleCollections.immutable(input);

      assertThat(set).isEmpty();
      assertThatThrownBy(set::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableCollectionFromMutableCollection() {
      Set<String> input = new HashSet<>();
      input.add("value");
      Set<String> set = LitesampleCollections.immutable(input);

      assertThat(set).containsExactly("value");
      assertThatThrownBy(set::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Nested
  @DisplayName("List")
  class LitesampleCollectionsListTest {

    @Test
    void shouldGetEmptyImmutableCollectionFromNullCollection() {
      List<Object> input = null;
      List<Object> list = LitesampleCollections.immutable(input);

      assertThat(list).isEmpty();
      assertThatThrownBy(list::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableCollectionFromMutableCollection() {
      List<String> input = new ArrayList<>();
      input.add("value");
      List<String> list = LitesampleCollections.immutable(input);

      assertThat(list).containsExactly("value");
      assertThatThrownBy(list::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Nested
  @DisplayName("Map")
  class LitesampleMapTest {

    @Test
    void shouldGetEmptyImmutableMapFromNullMap() {
      Map<Object, Object> input = null;
      Map<Object, Object> map = LitesampleCollections.immutable(input);

      assertThat(map).isEmpty();
      assertThatThrownBy(map::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableMapFromMutableMap() {
      Map<String, String> input = new HashMap<>();
      input.put("key", "value");
      Map<String, String> map = LitesampleCollections.immutable(input);

      assertThat(map).containsExactly(Map.entry("key", "value"));
      assertThatThrownBy(map::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }
}
