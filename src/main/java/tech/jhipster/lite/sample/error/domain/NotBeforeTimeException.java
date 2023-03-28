package tech.jhipster.lite.sample.error.domain;

import java.time.Instant;

public class NotBeforeTimeException extends AssertionException {

  private NotBeforeTimeException(String field, String message) {
    super(field, message);
  }

  public static NotBeforeTimeException notStrictlyBefore(String fieldName, Instant other) {
    return new NotBeforeTimeException(fieldName, message(fieldName, "must be strictly before", other));
  }

  public static NotBeforeTimeException notBefore(String fieldName, Instant other) {
    return new NotBeforeTimeException(fieldName, message(fieldName, "must be before", other));
  }

  private static String message(String fieldName, String hint, Instant other) {
    return new StringBuilder()
      .append("Time in \"")
      .append(fieldName)
      .append("\" ")
      .append(hint)
      .append(" ")
      .append(other)
      .append(" but wasn't")
      .toString();
  }

  @Override
  public AssertionErrorType type() {
    return AssertionErrorType.NOT_BEFORE_TIME;
  }
}
