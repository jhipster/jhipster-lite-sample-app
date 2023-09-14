package tech.jhipster.lite.sample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import tech.jhipster.lite.sample.shared.authentication.infrastructure.primary.TestSecurityConfiguration;

@ActiveProfiles("test")
@WithMockUser
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DisplayNameGeneration(ReplaceCamelCase.class)
@SpringBootTest(classes = { LitesampleApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
  @AliasFor(annotation = SpringBootTest.class)
  String[] properties() default {};
}
