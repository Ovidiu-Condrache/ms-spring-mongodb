package com.github.oc.msspringmongodb.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "truncatedDateTimeProvider")
@ConditionalOnExpression("#{'${ms.scenario}' == '5'}")
public class DateTimeProviderConfig {

    @Bean(name = "truncatedDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now().truncatedTo(ChronoUnit.MILLIS));
    }
}
