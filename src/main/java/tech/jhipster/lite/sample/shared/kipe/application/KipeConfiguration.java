package tech.jhipster.lite.sample.shared.kipe.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

@Configuration
class KipeConfiguration {

  @Bean
  protected MethodSecurityExpressionHandler createExpressionHandler(@Lazy AccessEvaluator evaluator) {
    return new KipeMethodSecurityExpressionHandler(evaluator);
  }
}
