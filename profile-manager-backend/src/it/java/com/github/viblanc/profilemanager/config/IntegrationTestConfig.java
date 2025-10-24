package com.github.viblanc.profilemanager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class IntegrationTestConfig {
	
	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> postgres() {
		return new PostgreSQLContainer<>("postgres:17-alpine");
	}

	@Bean
	DynamicPropertyRegistrar addCorsOrigins() {
		return register -> register.add("allowed.origins", () -> "http://localhost:4200");
	}

}
