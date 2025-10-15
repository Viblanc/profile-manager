package com.github.viblanc.profilemanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.github.viblanc.profilemanager.entity.User;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		// disable HTTP methods that will change data for the following resources
		HttpMethod[] unsupportedMethods = { HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.POST, HttpMethod.DELETE };
		Class<?>[] resources = { User.class };

		for (Class<?> clazz : resources) {
			disableHttpMethodsForResource(clazz, config, unsupportedMethods);
		}
	}

	private void disableHttpMethodsForResource(Class<?> clazz, RepositoryRestConfiguration config,
			HttpMethod[] disabledHttpMethods) {
		config.getExposureConfiguration().forDomainType(clazz)
				.withItemExposure((metadata, httpMethods) -> httpMethods.disable(disabledHttpMethods))
				.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(disabledHttpMethods));
	}
}
