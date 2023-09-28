package tech.jhipster.lite.sample;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.function.Predicate;
import org.slf4j.LoggerFactory;

public final class LogsSpy {

  private Logger logger;
  private ListAppender<ILoggingEvent> appender;
  private Level initialLevel;

  void prepare() {
    appender = new ListAppender<>();
    logger = (Logger) LoggerFactory.getLogger("tech.jhipster.lite.sample");
    logger.addAppender(appender);
    initialLevel = logger.getLevel();
    logger.setLevel(Level.TRACE);
    appender.start();
  }

  void reset() {
    logger.setLevel(initialLevel);
    logger.detachAppender(appender);
  }

  public LogsSpy shouldHave(Level level, String content) {
    assertThat(appender.list).anyMatch(withLog(level, content));

    return this;
  }

  public LogsSpy shouldHave(Level level, String content, int count) {
    assertThat(appender.list.stream().filter(withLog(level, content))).hasSize(count);

    return this;
  }

  public LogsSpy shouldNotHave(Level level, String content) {
    assertThat(appender.list).noneMatch(withLog(level, content));

    return this;
  }

  private Predicate<ILoggingEvent> withLog(Level level, String content) {
    return event -> level.equals(event.getLevel()) && event.toString().contains(content);
  }
}
