package tech.jhipster.lite.sample.wire.async.infrastructure.secondary;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
class AsyncConfiguration {}
