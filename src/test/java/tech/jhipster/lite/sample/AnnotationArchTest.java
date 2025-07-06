package tech.jhipster.lite.sample;

import static com.tngtech.archunit.base.DescribedPredicate.*;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.*;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

@UnitTest
class AnnotationArchTest {

  private static final String ROOT_PACKAGE = "tech.jhipster.lite.sample";
  private static final String ROOT_PACKAGE_PROJECT = ROOT_PACKAGE + "..";

  // @formatter:off
  private final JavaClasses classes = new ClassFileImporter()
    .importPackages(ROOT_PACKAGE)
    .that(
      are(
        not(equivalentTo(UnitTest.class))
        .and(not(equivalentTo(IntegrationTest.class))
        .and(not(equivalentTo(ComponentTest.class))))
      )
    );

  // @formatter:on

  @Test
  void shouldHaveUnitTestOrComponentTestAnnotation() {
    // @formatter:off
    classes()
      .that()
        .resideInAnyPackage(ROOT_PACKAGE_PROJECT)
        .and().haveSimpleNameEndingWith("Test")
        .and(not(simpleNameEndingWith("IT")))
        .and().areTopLevelClasses()
      .should().beMetaAnnotatedWith(UnitTest.class)
      .orShould().beMetaAnnotatedWith(ComponentTest.class)
      .orShould().beInterfaces()
      .check(classes);
    // @formatter:on
  }

  @Test
  void shouldHaveIntegrationTestAnnotation() {
    // @formatter:off
    classes()
      .that()
        .resideInAnyPackage(ROOT_PACKAGE_PROJECT)
        .and().haveSimpleNameEndingWith("IT")
        .and().areTopLevelClasses()
      .should().beMetaAnnotatedWith(IntegrationTest.class)
      .check(classes);
    // @formatter:on
  }
}
