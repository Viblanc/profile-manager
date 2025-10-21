package com.github.viblanc.profilemanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.MyTestConfiguration;
import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.mappers.UserMapper;
import com.github.viblanc.profilemanager.repository.UserRepository;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Import(MyTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    private UserType userType = new UserType(null, "Admin", null);

    private UserTypeDto userTypeDto;

    @BeforeEach
    void setUp() {
        userTypeRepository.save(userType);
        userTypeDto = new UserTypeDto(userType.getId(), userType.getName());
    }

    @AfterEach
    void tearDown() {
    	userRepository.deleteAll();
        userTypeRepository.deleteAll();
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = List.of(new User(null, "John", "Doe", "john@doe.mail", userType),
                new User(null, "Jane", "Doe", "jane@doe.mail", userType));
        userRepository.saveAll(users);

        List<UserDto> expected = users.stream().map(mapper::toDto).toList();
        List<UserDto> actual = userService.findAll();

        assertAll(() -> assertEquals(2, actual.size()), () -> assertEquals(expected, actual),
                () -> assertThat(expected, equalTo(actual)));
    }

    @Test
    void shouldGetUserById() {
        User user = new User(null, "John", "Doe", "john@doe.mail", userType);
        UserDto expected = mapper.toDto(userRepository.save(user));
        UserDto actual = userService.getUser(expected.id());

        assertEquals(expected, actual);
    }

    @Test
    void shouldAddUser() {
        UserDto expected = new UserDto(null, "John", "Doe", "john@doe.mail", userTypeDto);
        UserDto actual = userService.addUser(expected);

        assertAll(() -> assertEquals(expected.firstName(), actual.firstName()),
                () -> assertEquals(expected.lastName(), actual.lastName()),
                () -> assertEquals(expected.email(), actual.email()),
                () -> assertEquals(expected.userType().name(), actual.userType().name()));
    }

    @Test
    void shouldUpdateUserType() {
        User user = userRepository.save(new User(null, "John", "Doe", "john@doe.mail", userType));
        UserDto expected = new UserDto(user.getId(), "Jane", "Doe", "jane@doe.mail", userTypeDto);
        UserDto actual = userService.editUser(user.getId(), expected);

        assertAll(() -> assertEquals(expected.firstName(), actual.firstName()),
                () -> assertEquals(expected.lastName(), actual.lastName()),
                () -> assertEquals(expected.email(), actual.email()),
                () -> assertEquals(expected.userType().name(), actual.userType().name()));
    }

    @Test
    void shouldDeleteUserType() {
        User user = new User(null, "John", "Doe", "john@doe.mail", userType);
        userRepository.save(user);
        Long id = user.getId();
        userService.removeUser(id);

        assertEquals(true, userRepository.findById(id).isEmpty());
    }

}
