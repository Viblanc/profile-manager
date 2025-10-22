package com.github.viblanc.profilemanager.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.Assert.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.MyTestConfiguration;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Import(MyTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTypeControllerIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
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

        assertAll(() -> assertEquals(2, actual.size()), () -> assertEquals(expected, actual),
                () -> assertThat(expected, equalTo(actual)));
    }

    @Test
    void shouldGetUserTypeById() {
        UserType userType = new UserType(null, "Admin", null);
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
        UserTypeDto userType = new UserTypeDto(null, "User");

        given().contentType(ContentType.JSON)
            .with()
            .body(userType)
            .when()
            .post("/api/user_types")
            .then()
            .statusCode(201)
            .assertThat()
            .body("name", equalTo("User"));
    }

    @Test
    void shouldUpdateUserType() {
        UserType userType = new UserType(null, "User", null);
        userTypeRepository.save(userType);
        final int id = Math.toIntExact(userType.getId());
        final String userTypeName = "Admin";
        UserTypeDto newUserType = new UserTypeDto(userType.getId(), userTypeName);

        given().contentType(ContentType.JSON)
            .with()
            .body(newUserType)
            .when()
            .put("/api/user_types/{id}", userType.getId())
            .then()
            .statusCode(201)
            .assertThat()
            .body("id", equalTo(id))
            .body("name", equalTo(userTypeName));
    }

    @Test
    void shouldDeleteUserType() {
        UserType userType = new UserType(null, "User", null);
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
        UserType userType = new UserType(null, "Admin", null);
        userTypeRepository.save(userType);
        UserTypeDto newUserType = new UserTypeDto(null, "Admin");

        given().contentType(ContentType.JSON)
            .with()
            .body(newUserType)
            .when()
            .post("/api/user_types")
            .then()
            .statusCode(409)
            .assertThat()
            .body("message", equalTo("A user type with the name Admin already exists."));
    }
    
    @Test
    void shouldGetError_whenUpdatingUserType_withDifferentIds() {
    	UserType userType = new UserType(null, "Admin", null);
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
        UserType userType = new UserType(null, "Admin", null);
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