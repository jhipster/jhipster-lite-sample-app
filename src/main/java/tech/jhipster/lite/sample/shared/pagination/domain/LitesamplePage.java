package tech.jhipster.lite.sample.shared.pagination.domain;

import java.util.List;
import java.util.function.Function;
import tech.jhipster.lite.sample.shared.collection.domain.LitesampleCollections;
import tech.jhipster.lite.sample.shared.error.domain.Assert;

public class LitesamplePage<T> {

  private static final int MINIMAL_PAGE_COUNT = 1;

  private final List<T> content;
  private final int currentPage;
  private final int pageSize;
  private final long totalElementsCount;

  private LitesamplePage(LitesamplePageBuilder<T> builder) {
    content = LitesampleCollections.immutable(builder.content);
    currentPage = builder.currentPage;
    pageSize = buildPageSize(builder.pageSize);
    totalElementsCount = buildTotalElementsCount(builder.totalElementsCount);
  }

  private int buildPageSize(int pageSize) {
    if (pageSize == -1) {
      return content.size();
    }

    return pageSize;
  }

  private long buildTotalElementsCount(long totalElementsCount) {
    if (totalElementsCount == -1) {
      return content.size();
    }

    return totalElementsCount;
  }

  public static <T> LitesamplePage<T> singlePage(List<T> content) {
    return builder(content).build();
  }

  public static <T> LitesamplePageBuilder<T> builder(List<T> content) {
    return new LitesamplePageBuilder<>(content);
  }

  public static <T> LitesamplePage<T> of(List<T> elements, LitesamplePageable pagination) {
    Assert.notNull("elements", elements);
    Assert.notNull("pagination", pagination);

    List<T> content = elements.subList(
      Math.min(pagination.offset(), elements.size()),
      Math.min(pagination.offset() + pagination.pageSize(), elements.size())
    );

    return builder(content).currentPage(pagination.page()).pageSize(pagination.pageSize()).totalElementsCount(elements.size()).build();
  }

  public List<T> content() {
    return content;
  }

  public int currentPage() {
    return currentPage;
  }

  public int pageSize() {
    return pageSize;
  }

  public long totalElementsCount() {
    return totalElementsCount;
  }

  public int pageCount() {
    if (totalElementsCount > 0) {
      return (int) Math.ceil(totalElementsCount / (float) pageSize);
    }

    return MINIMAL_PAGE_COUNT;
  }

  public boolean hasPrevious() {
    return currentPage > 0;
  }

  public boolean hasNext() {
    return isNotLast();
  }

  public boolean isNotLast() {
    return currentPage + 1 < pageCount();
  }

  public <R> LitesamplePage<R> map(Function<T, R> mapper) {
    Assert.notNull("mapper", mapper);

    return builder(content().stream().map(mapper).toList())
      .currentPage(currentPage)
      .pageSize(pageSize)
      .totalElementsCount(totalElementsCount)
      .build();
  }

  public static class LitesamplePageBuilder<T> {

    private final List<T> content;
    private int currentPage;
    private int pageSize = -1;
    private long totalElementsCount = -1;

    private LitesamplePageBuilder(List<T> content) {
      this.content = content;
    }

    public LitesamplePageBuilder<T> pageSize(int pageSize) {
      this.pageSize = pageSize;

      return this;
    }

    public LitesamplePageBuilder<T> currentPage(int currentPage) {
      this.currentPage = currentPage;

      return this;
    }

    public LitesamplePageBuilder<T> totalElementsCount(long totalElementsCount) {
      this.totalElementsCount = totalElementsCount;

      return this;
    }

    public LitesamplePage<T> build() {
      return new LitesamplePage<>(this);
    }
  }
}
