package com.spring_cloud.eureka.client.company.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.spring_cloud.eureka.client.company.infrastructure.repository")
public class DatabaseConfig {
}
