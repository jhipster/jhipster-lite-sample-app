package tech.jhipster.lite.sample.kipe.application;

import java.util.function.Supplier;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import tech.jhipster.lite.sample.common.domain.Generated;

@Generated(reason = "Spring glue bean")
class KipeMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

  private final AccessEvaluator evaluator;

  private Object filterObject;
  private Object returnObject;
  private Object target;

  public KipeMethodSecurityExpressionRoot(Supplier<Authentication> authentication, AccessEvaluator evaluator) {
    super(authentication);
    this.evaluator = evaluator;
  }

  @Override
  public void setFilterObject(Object filterObject) {
    this.filterObject = filterObject;
  }

  @Override
  public Object getFilterObject() {
    return filterObject;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    this.returnObject = returnObject;
  }

  @Override
  public Object getReturnObject() {
    return returnObject;
  }

  void setThis(Object target) {
    this.target = target;
  }

  @Override
  public Object getThis() {
    return target;
  }

  public boolean can(String action, Object item) {
    return evaluator.can(getAuthentication(), action, item);
  }
}
