package tech.jhipster.lite.sample;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayNameGenerator;

public class ReplaceCamelCase extends DisplayNameGenerator.Standard {

  @Override
  public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
    return this.replaceCapitals(testMethod.getName());
  }

  private String replaceCapitals(String name) {
    name = name.replaceAll("([A-Z])", " $1");
    name = name.replaceAll("(\\d+)", " $1");
    name = name.toLowerCase();
    return name;
  }
}
