package tech.jhipster.lite.sample.shared.enumeration.domain;

class UnmappableEnumException extends RuntimeException {

  public <T extends Enum<T>, U extends Enum<U>> UnmappableEnumException(Class<T> from, Class<U> to) {
    super("Can't map " + from + " to " + to);
  }
}
