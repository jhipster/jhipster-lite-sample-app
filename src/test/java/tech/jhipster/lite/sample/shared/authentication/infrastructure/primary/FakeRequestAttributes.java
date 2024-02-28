package tech.jhipster.lite.sample.shared.authentication.infrastructure.primary;

import org.springframework.web.context.request.AbstractRequestAttributes;

public class FakeRequestAttributes extends AbstractRequestAttributes {

  @Override
  public void requestCompleted() {
    super.requestCompleted();
  }

  @Override
  protected void updateAccessedSessionAttributes() {
    // do nothing
  }

  @Override
  public Object getAttribute(String name, int scope) {
    return null;
  }

  @Override
  public void setAttribute(String name, Object value, int scope) {
    // do nothing
  }

  @Override
  public void removeAttribute(String name, int scope) {
    // do nothing
  }

  @Override
  public String[] getAttributeNames(int scope) {
    return new String[0];
  }

  @Override
  public void registerDestructionCallback(String name, Runnable callback, int scope) {
    // do nothing
  }

  @Override
  public Object resolveReference(String key) {
    return null;
  }

  @Override
  public String getSessionId() {
    return null;
  }

  @Override
  public Object getSessionMutex() {
    return null;
  }
}
