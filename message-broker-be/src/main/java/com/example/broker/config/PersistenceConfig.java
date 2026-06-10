package com.example.broker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.broker.repository.jpa")
@EnableMongoRepositories(basePackages = "com.example.broker.repository.mongo")
public class PersistenceConfig {
}
