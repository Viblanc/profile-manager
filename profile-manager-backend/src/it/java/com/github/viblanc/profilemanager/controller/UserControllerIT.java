package com.github.viblanc.profilemanager.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.MyTestConfiguration;
import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.mappers.UserMapper;
import com.github.viblanc.profilemanager.repository.UserRepository;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Import(MyTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    private User user;
    
    private UserDto userDto;
    
    private UserType userType;

    private UserTypeDto userTypeDto;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        
        userType = new UserType(null, "Admin", null);
        user = new User(null, "John", "Doe", "john@doe.mail", userType);
        userTypeRepository.save(userType);
        
        userTypeDto = new UserTypeDto(userType.getId(), userType.getName());
        userDto = new UserDto(null, "John", "Doe", "john@doe.mail", userTypeDto);
    }

    @AfterEach
    void tearDown() {
        userTypeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = List.of(new User(null, "John", "Doe", "john@doe.mail", userType),
                new User(null, "Jane", "Doe", "jane@doe.mail", userType));
        userRepository.saveAll(users);

        List<UserDto> actual = given().contentType(ContentType.JSON)
            .when()
            .get("/api/users")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList(".", UserDto.class);

        List<UserDto> expected = users.stream().map(mapper::toDto).toList();

        assertThat(actual).hasSize(2).containsAll(expected);
    }

    @Test
    void shouldGetUserById() {
        userRepository.save(user);

        given().contentType(ContentType.JSON)
            .when()
            .get("/api/users/{id}", user.getId())
            .then()
            .statusCode(200)
            .body("firstName", equalTo("John"), "lastName", equalTo("Doe"), "email", equalTo("john@doe.mail"),
                    "userType.name", equalTo("Admin"));
    }

    @Test
    void shouldAddUser() {
        UserDto expected = userDto;

        UserDto actual = given().contentType(ContentType.JSON)
            .with()
            .body(expected)
            .when()
            .post("/api/users")
            .then()
            .statusCode(201)
            .extract()
            .as(UserDto.class);

        assertThat(actual).extracting(UserDto::firstName, UserDto::lastName, UserDto::email, UserDto::userType)
            .containsExactly(expected.firstName(), expected.lastName(), expected.email(), expected.userType());
    }

    @Test
    void shouldUpdateUserType() {
        userRepository.save(user);

        UserDto expected = new UserDto(user.getId(), "Jane", "Doe", "jane@doe.mail", userTypeDto);

        UserDto actual = given().contentType(ContentType.JSON)
            .with()
            .body(expected)
            .when()
            .put("/api/users/{id}", user.getId())
            .then()
            .statusCode(201)
            .extract()
            .as(UserDto.class);

        assertThat(actual).extracting(UserDto::firstName, UserDto::lastName, UserDto::email, UserDto::userType)
            .containsExactly(expected.firstName(), expected.lastName(), expected.email(), expected.userType());
    }

    @Test
    void shouldDeleteUserType() {
        userRepository.save(user);

        given().contentType(ContentType.JSON).when().delete("/api/users/{id}", user.getId()).then().statusCode(204);
    }

    @Test
    void shouldGetError_whenUserNotFound() {
        given().contentType(ContentType.JSON)
            .when()
            .get("/api/users/1")
            .then()
            .statusCode(404)
            .assertThat()
            .body("message", equalTo("User with id 1 not found."));
    }

    @Test
    void shouldGetError_whenAddingUser_withEmailAlreadyExists() {
        userRepository.save(user);

        UserDto newUser = new UserDto(null, "Jean", "Dupont", user.getEmail(), userTypeDto);

        given().contentType(ContentType.JSON)
            .with()
            .body(newUser)
            .when()
            .post("/api/users")
            .then()
            .statusCode(409)
            .assertThat()
            .body("message", equalTo("User with email " + user.getEmail() + " already exists."));
    }

    @Test
    void shouldGetError_whenAddingUser_withUnknownUserType() {
        UserDto newUser = new UserDto(null, "Jean", "Dupont", "john@doe.mail", new UserTypeDto(null, "Guest"));

        given().contentType(ContentType.JSON)
            .with()
            .body(newUser)
            .when()
            .post("/api/users")
            .then()
            .statusCode(404)
            .assertThat()
            .body("message", equalTo("User type with name Guest does not exist."));
    }

    @Test
    void shouldGetError_whenUpdatingUser_withDifferentIds() {
        userRepository.save(user);

        UserDto updatedUser = new UserDto(user.getId() + 1L, "Jean", "Dupont", "jdupont@mail.fr", userTypeDto);

        given().contentType(ContentType.JSON)
            .with()
            .body(updatedUser)
            .when()
            .put("/api/users/{id}", user.getId())
            .then()
            .statusCode(400)
            .assertThat()
            .body("message", equalTo("IDs don't match."));
    }

    @Test
    void shouldGetError_whenUpdatingUser_notFound() {
        UserDto newUser = new UserDto(1L, "Jean", "Dupont", "john@doe.mail", userTypeDto);

        given().contentType(ContentType.JSON)
            .with()
            .body(newUser)
            .when()
            .put("/api/users/1")
            .then()
            .statusCode(404)
            .assertThat()
            .body("message", equalTo("User with id 1 not found."));
    }

    @Test
    void shouldGetError_whenUpdatingUser_withEmailAlreadyExists() {
        userRepository.save(user);
        User user2 = new User(null, "Jeanne", "Dupont", "jdupont@mail.fr", userType);
        userRepository.save(user2);

        UserDto updatedUser = new UserDto(user.getId(), "Jean", "Dupont", "jdupont@mail.fr", userTypeDto);

        given().contentType(ContentType.JSON)
            .with()
            .body(updatedUser)
            .when()
            .put("/api/users/{id}", user.getId())
            .then()
            .statusCode(409)
            .assertThat()
            .body("message", equalTo("User with email jdupont@mail.fr already exists."));
    }

    @Test
    void shouldGetError_whenUpdatingUser_withUnknownUserType() {
        userRepository.save(user);

        UserDto updatedUser = new UserDto(user.getId(), "Jean", "Dupont", "john@doe.mail",
                new UserTypeDto(null, "Guest"));

        given().contentType(ContentType.JSON)
            .with()
            .body(updatedUser)
            .when()
            .put("/api/users/{id}", user.getId())
            .then()
            .statusCode(404)
            .assertThat()
            .body("message", equalTo("User type with name Guest does not exist."));
    }

}
