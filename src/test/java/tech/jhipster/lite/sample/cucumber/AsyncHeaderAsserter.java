package tech.jhipster.lite.sample.cucumber;

import java.time.Duration;

public class AsyncHeaderAsserter implements HeaderAsserter<AsyncResponseAsserter> {

  private final Duration maxTime;
  private final AsyncResponseAsserter responseAsserter;
  private final HeaderAssertions assertions;

  AsyncHeaderAsserter(AsyncResponseAsserter responseAsserter, String header, Duration maxTime) {
    this.responseAsserter = responseAsserter;
    this.maxTime = maxTime;
    assertions = new HeaderAssertions(header);
  }

  @Override
  public AsyncHeaderAsserter containing(String value) {
    Awaiter.await(maxTime, () -> assertions.containing(value));

    return this;
  }

  @Override
  public AsyncHeaderAsserter startingWith(String prefix) {
    Awaiter.await(maxTime, () -> assertions.startingWith(prefix));

    return this;
  }

  @Override
  public AsyncResponseAsserter and() {
    return responseAsserter;
  }
}
