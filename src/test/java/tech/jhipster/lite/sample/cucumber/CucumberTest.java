package tech.jhipster.lite.sample.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import tech.jhipster.lite.sample.ComponentTest;

@ComponentTest
@RunWith(Cucumber.class)
@CucumberOptions(
  glue = "tech.jhipster.lite.sample",
  plugin = {
    "pretty", "json:target/cucumber/cucumber.json", "html:target/cucumber/cucumber.htm", "junit:target/cucumber/TEST-cucumber.xml",
  },
  features = "src/test/features"
)
public class CucumberTest {}
