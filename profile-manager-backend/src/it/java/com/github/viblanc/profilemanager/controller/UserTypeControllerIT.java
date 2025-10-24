package com.github.viblanc.profilemanager.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.IntegrationTestConfig;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Import(IntegrationTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTypeControllerIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private UserType userType;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userType = new UserType(null, "Admin", null);
    }
    
    @AfterEach
    void tearDown() {
    	userTypeRepository.deleteAll();
    }

    @Test
    void shouldGetAllUserTypes() {
        List<UserType> userTypes = List.of(new UserType(null, "Admin", null), new UserType(null, "User", null));
        userTypeRepository.saveAll(userTypes);

        List<UserTypeDto> actual = given().contentType(ContentType.JSON)
            .when()
            .get("/api/user_types")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList(".", UserTypeDto.class);

        List<UserTypeDto> expected = userTypes.stream().map(u -> new UserTypeDto(u.getId(), u.getName())).toList();

        assertThat(actual).hasSize(2).containsExactlyElementsOf(expected);
    }

    @Test
    void shouldGetUserTypeById() {
        userTypeRepository.save(userType);

        given().contentType(ContentType.JSON)
            .when()
            .get("/api/user_types/{id}", userType.getId())
            .then()
            .statusCode(200)
            .body("name", equalTo("Admin"));
    }

    @Test
    void shouldAddUserType() {
        UserTypeDto userTypeDto = new UserTypeDto(null, "User");

        given().contentType(ContentType.JSON)
            .with()
            .body(userTypeDto)
            .when()
            .post("/api/user_types")
            .then()
            .statusCode(201)
            .assertThat()
            .body("name", equalTo("User"));
    }

    @Test
    void shouldUpdateUserType() {
        userTypeRepository.save(userType);
        final String userTypeName = "Admin";
        UserTypeDto newUserType = new UserTypeDto(userType.getId(), userTypeName);

        UserTypeDto actual = given().contentType(ContentType.JSON)
            .with()
            .body(newUserType)
            .when()
            .put("/api/user_types/{id}", userType.getId())
            .then()
            .statusCode(201)
            .extract()
            .as(UserTypeDto.class);

        assertThat(actual).extracting(UserTypeDto::id, UserTypeDto::name)
            .containsExactly(userType.getId(), userTypeName);
    }

    @Test
    void shouldDeleteUserType() {
        userTypeRepository.save(userType);

        given().contentType(ContentType.JSON)
            .when()
            .delete("/api/user_types/{id}", userType.getId())
            .then()
            .statusCode(204);
    }

    @Test
    void shouldGetError_whenUserTypeNotFound() {
        given().contentType(ContentType.JSON)
            .when()
            .get("/api/user_types/1")
            .then()
            .statusCode(404)
            .assertThat()
            .body("message", equalTo("User type with id 1 not found."));
    }

    @Test
    void shouldGetError_whenAddingUserType_withNameAlreadyExists() {
        userTypeRepository.save(userType);
        UserTypeDto newUserType = new UserTypeDto(null, userType.getName());

        given().contentType(ContentType.JSON)
            .with()
            .body(newUserType)
            .when()
            .post("/api/user_types")
            .then()
            .statusCode(409)
            .assertThat()
            .body("message", equalTo("A user type with the name " + userType.getName() + " already exists."));
    }

    @Test
    void shouldGetError_whenUpdatingUserType_withDifferentIds() {
        userTypeRepository.save(userType);
        UserTypeDto updatedUserType = new UserTypeDto(2L, "User");

        given().contentType(ContentType.JSON)
            .with()
            .body(updatedUserType)
            .when()
            .put("/api/user_types/1")
            .then()
            .statusCode(400)
            .assertThat()
            .body("message", equalTo("IDs don't match."));
    }

    @Test
    void shouldGetError_whenUpdatingUserType_notFound() {
        UserTypeDto updatedUserType = new UserTypeDto(1L, "User");

        given().contentType(ContentType.JSON)
            .with()
            .body(updatedUserType)
            .when()
            .put("/api/user_types/1")
            .then()
            .statusCode(404)
            .assertThat()
            .body("message", equalTo("User type with id 1 not found."));
    }

    @Test
    void shouldGetError_whenUpdatingUserType_withNameAlreadyExists() {
        userTypeRepository.save(userType);
        // add user type with name "User"
        userTypeRepository.save(new UserType(null, "User", null));
        UserTypeDto updatedUserType = new UserTypeDto(userType.getId(), "User");

        given().contentType(ContentType.JSON)
            .with()
            .body(updatedUserType)
            .when()
            .put("/api/user_types/{id}", userType.getId())
            .then()
            .statusCode(409)
            .assertThat()
            .body("message", equalTo("A user type with the name User already exists."));
    }

}