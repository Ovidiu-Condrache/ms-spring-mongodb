package com.github.oc.msspringmongodb.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
@ConditionalOnExpression("#{'${ms.scenario}' == '3' || '${ms.scenario}' == '5'}")
public class MongoConfig {
}
