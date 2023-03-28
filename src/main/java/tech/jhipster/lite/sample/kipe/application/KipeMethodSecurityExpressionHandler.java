package tech.jhipster.lite.sample.kipe.application;

import java.lang.reflect.Method;
import java.util.function.Supplier;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

class KipeMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

  private final AccessEvaluator evaluator;

  KipeMethodSecurityExpressionHandler(AccessEvaluator evaluator) {
    this.evaluator = evaluator;
  }

  @Override
  public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
    MethodSecurityExpressionOperations root = buildExpressionRoot(authentication, mi);

    MethodBasedEvaluationContext ctx = new MethodBasedEvaluationContext(
      root,
      getSpecificMethod(mi),
      mi.getArguments(),
      getParameterNameDiscoverer()
    );

    ctx.setBeanResolver(getBeanResolver());

    return ctx;
  }

  private static Method getSpecificMethod(MethodInvocation mi) {
    return AopUtils.getMostSpecificMethod(mi.getMethod(), AopProxyUtils.ultimateTargetClass(mi.getThis()));
  }

  private MethodSecurityExpressionOperations buildExpressionRoot(Supplier<Authentication> authentication, MethodInvocation invocation) {
    KipeMethodSecurityExpressionRoot root = new KipeMethodSecurityExpressionRoot(authentication, evaluator);

    root.setThis(invocation.getThis());
    root.setPermissionEvaluator(getPermissionEvaluator());
    root.setTrustResolver(getTrustResolver());
    root.setRoleHierarchy(getRoleHierarchy());
    root.setDefaultRolePrefix(getDefaultRolePrefix());

    return root;
  }
}
