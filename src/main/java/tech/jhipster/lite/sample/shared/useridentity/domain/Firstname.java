package tech.jhipster.lite.sample.shared.useridentity.domain;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public record Firstname(String firstname) implements Comparable<Firstname> {
  private static final Pattern FIRSTNAME_PATTERN = Pattern.compile("(\\p{L}+)(\\P{L}*)");

  public Firstname(String firstname) {
    Assert.field("firstname", firstname).notBlank().maxLength(150);

    this.firstname = buildFirstname(firstname);
  }

  private String buildFirstname(String firstname) {
    return FIRSTNAME_PATTERN.matcher(firstname).results().map(toCapitalizedWord()).collect(Collectors.joining());
  }

  private Function<MatchResult, String> toCapitalizedWord() {
    return result -> StringUtils.capitalize(result.group(1).toLowerCase()) + result.group(2);
  }

  public String get() {
    return firstname();
  }

  @Override
  public int compareTo(Firstname other) {
    if (other == null) {
      return -1;
    }

    return firstname.compareTo(other.firstname);
  }
}
