package com.github.viblanc.profilemanager.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;
import com.github.viblanc.profilemanager.service.UserTypeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTypeControllerTest {

	@LocalServerPort
	private Integer port;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Autowired
	UserTypeService userTypeService;

	@Autowired
	UserTypeRepository userTypeRepository;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;
		userTypeRepository.deleteAll();
	}

	@Test
	void shouldGetAllUserTypes() {
		List<UserType> userTypes = List.of(new UserType(null, "Admin", null), new UserType(null, "User", null));
		userTypeRepository.saveAll(userTypes);

		given().contentType(ContentType.JSON)
			.when()
			.get("/api/user_types")
			.then()
			.statusCode(200)
			.body(".", hasSize(2));
	}

	@Test
	void shouldGetUserTypeById(Long id) {
		List<UserType> userTypes = List.of(new UserType(null, "Admin", null), new UserType(null, "User", null));
		userTypeRepository.saveAll(userTypes);

		given().contentType(ContentType.JSON)
			.when()
			.get("/api/user_types")
			.then()
			.statusCode(200)
			.body(".", hasSize(2));
	}

}