package com.github.viblanc.profilemanager.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.MyTestConfiguration;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Import(MyTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserTypeServiceIT {

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @BeforeEach
    void setUp() {
        userTypeRepository.deleteAll();
    }

    @Test
    void shouldGetAllUserTypes() {
        List<UserType> userTypes = List.of(new UserType(null, "Admin", null), new UserType(null, "User", null));
        userTypeRepository.saveAll(userTypes);

        List<UserTypeDto> expected = userTypes.stream().map(u -> new UserTypeDto(u.getId(), u.getName())).toList();
        List<UserTypeDto> actual = userTypeService.findAll();

        assertAll(() -> assertEquals(2, actual.size()), () -> assertEquals(expected, actual),
                () -> assertThat(expected, equalTo(actual)));
    }

    @Test
    void shouldGetUserTypeById() {
        UserType userType = new UserType(null, "Admin", null);
        userTypeRepository.save(userType);

        UserTypeDto userTypeDto = userTypeService.getUserType(userType.getId());

        assertAll(() -> assertEquals(userType.getId(), userTypeDto.id()),
                () -> assertEquals(userType.getName(), userTypeDto.name()));
    }

    @Test
    void shouldAddUserType() {
        UserTypeDto userType = new UserTypeDto(null, "User");
        UserTypeDto newUserType = userTypeService.addUserType(userType);

        assertAll(() -> assertEquals(userType.name(), newUserType.name()));
    }

    @Test
    void shouldUpdateUserType() {
        // create user type named 'User'
        UserType userType = new UserType(null, "User", null);
        userTypeRepository.save(userType);
        // change the name of the user type to 'Admin'
        UserTypeDto newUserTypeDto = new UserTypeDto(userType.getId(), "Admin");
        UserTypeDto updatedUserType = userTypeService.editUserType(userType.getId(), newUserTypeDto);

        assertEquals(newUserTypeDto, updatedUserType);
    }

    @Test
    void shouldDeleteUserType() {
        UserType userType = new UserType(null, "User", null);
        userTypeRepository.save(userType);
        Long id = userType.getId();

        userTypeService.deleteUserType(id);

        assertEquals(true, userTypeRepository.findById(id).isEmpty());
    }

}
