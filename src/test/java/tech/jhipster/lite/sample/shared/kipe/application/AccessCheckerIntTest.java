package tech.jhipster.lite.sample.shared.kipe.application;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.jhipster.lite.sample.IntegrationTest;

@IntegrationTest
class AccessCheckerIntTest {

  @Test
  void canCheckersShouldBeSpringComponents() {
    canCheckers().forEach(this::mustBeSpringBean);
  }

  @SuppressWarnings("rawtypes")
  private Set<Class<? extends AccessChecker>> canCheckers() {
    return new Reflections("tech.jhipster.lite.sample").getSubTypesOf(AccessChecker.class);
  }

  private void mustBeSpringBean(@SuppressWarnings("rawtypes") Class<? extends AccessChecker> checker) {
    List<Class<?>> annotationsClasses = Stream.of(checker.getAnnotations()).map(Annotation::annotationType).collect(Collectors.toList());

    assertThat(annotationsClasses)
      .withFailMessage("AccessCheckers must be spring beans (@Component or @Service) but <%s> isn't", checker.getCanonicalName())
      .containsAnyOf(Component.class, Service.class);
  }
}
