package tech.jhipster.lite.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import tech.jhipster.lite.sample.common.domain.ExcludeFromGeneratedCodeCoverage;

@SpringBootApplication
@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
public class LitesampleApp {

  private static final Logger log = LoggerFactory.getLogger(LitesampleApp.class);

  public static void main(String[] args) {
    Environment env = SpringApplication.run(LitesampleApp.class, args).getEnvironment();

    if (log.isInfoEnabled()) {
      log.info(ApplicationStartupTraces.of(env));
    }
  }
}
