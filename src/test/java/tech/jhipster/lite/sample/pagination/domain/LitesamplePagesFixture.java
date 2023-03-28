package tech.jhipster.lite.sample.pagination.domain;

import java.util.List;
import tech.jhipster.lite.sample.pagination.domain.LitesamplePage.LitesamplePageBuilder;

public final class LitesamplePagesFixture {

  private LitesamplePagesFixture() {}

  public static LitesamplePage<String> page() {
    return pageBuilder().build();
  }

  public static LitesamplePageBuilder<String> pageBuilder() {
    return LitesamplePage.builder(List.of("test")).currentPage(2).pageSize(10).totalElementsCount(21);
  }
}
