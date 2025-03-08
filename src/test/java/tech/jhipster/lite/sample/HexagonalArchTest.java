package tech.jhipster.lite.sample;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@UnitTest
class HexagonalArchTest {

  private static final String ROOT_PACKAGE = "tech.jhipster.lite.sample";
  private static final String SHARED_PACKAGES = ROOT_PACKAGE.concat(".shared..");
  private static final String WIRE_PACKAGES = ROOT_PACKAGE.concat(".wire..");

  private static final JavaClasses classes = new ClassFileImporter()
    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
    .importPackages(ROOT_PACKAGE);

  private static final Collection<String> businessContexts = packagesWithAnnotation(BusinessContext.class);
  private static final String[] businessContextsPackages = buildPackagesPatterns(businessContexts);

  private static final Collection<String> sharedKernels = packagesWithAnnotation(SharedKernel.class);
  private static final String[] sharedKernelsPackages = buildPackagesPatterns(sharedKernels);

  // the empty package is related to: https://github.com/TNG/ArchUnit/issues/191#issuecomment-507964792
  private static final String[] vanillaPackages = new String[] { "java..", "" };
  private static final String[] commonToolsAndUtilsPackages = new String[] { "org.apache.commons..", "org.jmolecules..", "org.slf4j.." };

  private static String[] buildPackagesPatterns(Collection<String> packages) {
    return packages.stream().map(path -> path + "..").toArray(String[]::new);
  }

  private static Collection<String> packagesWithAnnotation(Class<? extends Annotation> annotationClass) throws AssertionError {
    try {
      return Files.walk(rootPackagePath())
        .filter(path -> path.toString().endsWith("package-info.java"))
        .map(toPackageName())
        .map(path -> path.replaceAll("[\\/]", "."))
        .map(path -> path.replaceAll("[\\\\]", "."))
        .map(path -> path.replace("src.main.java.", ""))
        .map(toPackage())
        .filter(pack -> pack.getAnnotation(annotationClass) != null)
        .map(Package::getName)
        .toList();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  private static Path rootPackagePath() {
    return Stream.of(ROOT_PACKAGE.split("\\.")).map(Path::of).reduce(Path.of("src", "main", "java"), Path::resolve);
  }

  private static Function<Path, String> toPackageName() {
    return path -> {
      var stringPath = path.toString();
      return stringPath.substring(0, stringPath.lastIndexOf("."));
    };
  }

  private static Function<String, Package> toPackage() {
    return path -> {
      try {
        return Class.forName(path).getPackage();
      } catch (ClassNotFoundException e) {
        throw new AssertionError();
      }
    };
  }

  @Nested
  class BoundedContexts {

    @Test
    void shouldNotDependOnOtherBoundedContextDomains() {
      Stream.concat(businessContexts.stream(), sharedKernels.stream()).forEach(context -> {
        noClasses()
          .that()
          .resideInAnyPackage(context + "..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(otherBusinessContextsDomains(context))
          .because("Contexts can only depend on classes in the same context or shared kernels")
          .check(classes);
      });
    }

    @Test
    void shouldBeAnHexagonalArchitecture() {
      Stream.concat(businessContexts.stream(), sharedKernels.stream()).forEach(context ->
        Architectures.layeredArchitecture()
          .consideringOnlyDependenciesInAnyPackage(context + "..")
          .withOptionalLayers(true)
          .layer("domain models")
          .definedBy(context + ".domain..")
          .layer("domain services")
          .definedBy(context + ".domain..")
          .layer("application services")
          .definedBy(context + ".application..")
          .layer("primary adapters")
          .definedBy(context + ".infrastructure.primary..")
          .layer("secondary adapters")
          .definedBy(context + ".infrastructure.secondary..")
          .whereLayer("application services")
          .mayOnlyBeAccessedByLayers("primary adapters")
          .whereLayer("primary adapters")
          .mayNotBeAccessedByAnyLayer()
          .whereLayer("secondary adapters")
          .mayNotBeAccessedByAnyLayer()
          .because("Each bounded context should implement a hexagonal architecture")
          .check(classes)
      );
    }

    @Test
    void primaryJavaAdaptersShouldOnlyBeCalledFromSecondaries() {
      classes()
        .that()
        .resideInAPackage("..primary..")
        .and()
        .areMetaAnnotatedWith(Component.class)
        .and()
        .haveSimpleNameStartingWith("Java")
        .should()
        .onlyHaveDependentClassesThat()
        .resideInAPackage("..secondary..")
        .because(
          "To interact between two contexts, secondary from context 'A' should call a primary Java adapter (naming convention starting with 'Java') from context 'B'"
        )
        .check(classes);
    }

    private String[] otherBusinessContextsDomains(String context) {
      return businessContexts.stream().filter(other -> !context.equals(other)).map(name -> name + ".domain..").toArray(String[]::new);
    }
  }

  @Nested
  class Domain {

    @Test
    void shouldNotDependOnOutside() {
      classes()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(authorizedDomainPackages())
        .because("Domain model should only depend on domains and a very limited set of external dependencies")
        .check(classes);
    }

    private String[] authorizedDomainPackages() {
      return Stream.of(new String[] { "..domain.." }, vanillaPackages, commonToolsAndUtilsPackages, sharedKernelsPackages).reduce(
        new String[] {},
        ArrayUtils::addAll
      );
    }
  }

  @Nested
  class Application {

    @Test
    void shouldNotDependOnInfrastructure() {
      noClasses()
        .that()
        .resideInAPackage("..application..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..infrastructure..")
        .because("Application should only depend on domain, not on infrastructure")
        .check(classes);
    }
  }

  @Nested
  class Primary {

    @Test
    void shouldNotDependOnSecondary() {
      noClasses()
        .that()
        .resideInAPackage("..primary..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..secondary..")
        .because("Primary should not interact with secondary")
        .check(classes);
    }

    @Test
    void shouldNotHavePublicControllers() {
      noClasses().that().areMetaAnnotatedWith(Controller.class).should().bePublic().check(classes);
    }
  }

  @Nested
  class Secondary {

    @Test
    void shouldNotDependOnApplication() {
      noClasses()
        .that()
        .resideInAPackage("..infrastructure.secondary..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..application..")
        .because("Secondary should not depend on application")
        .check(classes);
    }

    @Test
    void shouldNotDependOnSameContextPrimary() {
      Stream.concat(businessContexts.stream(), sharedKernels.stream()).forEach(context -> {
        noClasses()
          .that()
          .resideInAPackage(context + ".infrastructure.secondary..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage(context + ".infrastructure.primary")
          .because("Secondary should not loop to its own context's primary")
          .check(classes);
      });
    }
  }

  @Nested
  class SharedKernels {

    @Test
    void topLevelSharedPackageShouldOnlyContainSharedKernels() {
      classes()
        .that()
        .haveSimpleName("package-info")
        .and()
        .resideInAPackage(SHARED_PACKAGES)
        .should()
        .beMetaAnnotatedWith(SharedKernel.class)
        .because(ROOT_PACKAGE + ".shared package should only contain shared kernels")
        .check(classes);
    }
  }

  @Nested
  class Wire {

    @Test
    void shouldNotDependOnBoundedContexts() {
      noClasses()
        .that()
        .resideInAPackage(WIRE_PACKAGES)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(businessContextsPackages)
        .because("Wire should not depend on business contexts or shared kernel should not depend")
        .check(classes);
    }

    @Test
    void boundedContextsAndSharedKernelsShouldNotDependOnWire() {
      noClasses()
        .that()
        .resideInAnyPackage(businessContextsOrSharedKernelsPackages())
        .and()
        .resideOutsideOfPackage(WIRE_PACKAGES)
        .should()
        .dependOnClassesThat()
        .resideInAPackage(WIRE_PACKAGES)
        .because("Business contexts and shared kernel should not depend on wire")
        .check(classes);
    }

    private static String[] businessContextsOrSharedKernelsPackages() {
      return ArrayUtils.addAll(businessContextsPackages, sharedKernelsPackages);
    }

    @Test
    void shouldNotHavePublicClasses() {
      noClasses().that().resideInAnyPackage(WIRE_PACKAGES).should().bePublic().check(classes);
    }
  }
}
