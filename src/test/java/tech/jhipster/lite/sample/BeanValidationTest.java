package tech.jhipster.lite.sample;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@UnitTest
class BeanValidationTest {

  private static final String ROOT_PACKAGE = "tech.jhipster.lite.sample";
  private static final Set<String> EXCLUDED_CONTROLLERS = Set.of(
    "ExceptionTranslatorTestController",
    "AuthenticationResource",
    "DummyResource"
  );
  private static final Set<Method> OBJECT_METHODS = Arrays.stream(Object.class.getMethods()).collect(Collectors.toUnmodifiableSet());
  private static final Set<Class<?>> controllers = new Reflections(
    new ConfigurationBuilder()
      .setUrls(ClasspathHelper.forPackage(ROOT_PACKAGE))
      .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes)
      .filterInputsBy(new FilterBuilder().includePackage(ROOT_PACKAGE))
  ).getTypesAnnotatedWith(RestController.class);

  @Test
  void shouldHaveValidatedAnnotationForAllParameters() {
    controllers
      .stream()
      .filter(controller -> !EXCLUDED_CONTROLLERS.contains(controller.getSimpleName()))
      .flatMap(toMethods())
      .filter(visibleMethods())
      .filter(controllerMethods())
      .forEach(checkValidatedAnnotation());
  }

  private Function<Class<?>, Stream<Method>> toMethods() {
    return controller -> Arrays.stream(controller.getMethods());
  }

  private Predicate<Method> visibleMethods() {
    return method -> !Modifier.isPrivate(method.getModifiers());
  }

  private Predicate<Method> controllerMethods() {
    return method -> !OBJECT_METHODS.contains(method);
  }

  private Consumer<Method> checkValidatedAnnotation() {
    return method ->
      Arrays.stream(method.getParameters())
        .filter(checkedTypes())
        .forEach(
          parameter ->
            assertThat(parameter.getAnnotations())
              .as(errorMessage(method, parameter))
              .extracting(Annotation::annotationType)
              .anyMatch(Validated.class::equals)
        );
  }

  private String errorMessage(Method method, Parameter parameter) {
    return (
      "Missing @Validated annotation in " +
      method.getDeclaringClass().getSimpleName() +
      " on method " +
      method.getName() +
      " parameter of type " +
      parameter.getType().getSimpleName()
    );
  }

  private Predicate<Parameter> checkedTypes() {
    return parameter -> {
      Class<?> parameterClass = parameter.getType();
      return !parameterClass.isPrimitive() && parameterClass.getName().startsWith(ROOT_PACKAGE);
    };
  }
}
