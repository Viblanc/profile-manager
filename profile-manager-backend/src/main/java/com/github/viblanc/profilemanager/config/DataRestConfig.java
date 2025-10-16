package com.github.viblanc.profilemanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.github.viblanc.profilemanager.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {
	private EntityManager entityManager;

	public DataRestConfig(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		// gather entity types in an array
		Class<?>[] entityTypes = entityManager.getMetamodel().getEntities().stream().map(EntityType::getJavaType)
				.toArray(Class[]::new);

		// expose entity ids
		config.exposeIdsFor(entityTypes);
	}
}
