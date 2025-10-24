package com.github.viblanc.profilemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.github.viblanc.profilemanager.config.IntegrationTestConfig;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Import(IntegrationTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserTypeServiceIT {

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private UserTypeRepository userTypeRepository;
    
    @AfterEach
    void tearDown() {
        userTypeRepository.deleteAll();
    }

    @Test
    void shouldGetAllUserTypes() {
        List<UserType> userTypes = List.of(new UserType(null, "Admin", null), new UserType(null, "User", null));
        userTypeRepository.saveAll(userTypes);

        List<UserTypeDto> expected = userTypes.stream().map(u -> new UserTypeDto(u.getId(), u.getName())).toList();
        List<UserTypeDto> actual = userTypeService.findAll();

        assertThat(actual).hasSize(2).containsExactlyElementsOf(expected);
    }

    @Test
    void shouldGetUserTypeById() {
        UserType userType = new UserType(null, "Admin", null);
        userTypeRepository.save(userType);

        UserTypeDto expected = new UserTypeDto(userType.getId(), "Admin");
        UserTypeDto actual = userTypeService.getUserType(userType.getId());

        assertThat(actual).extracting(UserTypeDto::id, UserTypeDto::name)
            .containsExactly(expected.id(), expected.name());
    }

    @Test
    void shouldAddUserType() {
        UserTypeDto userType = new UserTypeDto(null, "User");
        UserTypeDto actual = userTypeService.addUserType(userType);

        assertThat(actual.name()).isEqualTo("User");
    }

    @Test
    void shouldUpdateUserType() {
        // create user type named 'User'
        UserType userType = new UserType(null, "User", null);
        userTypeRepository.save(userType);
        // change the name of the user type to 'Admin'
        UserTypeDto expected = new UserTypeDto(userType.getId(), "Admin");
        UserTypeDto actual = userTypeService.editUserType(userType.getId(), expected);

        assertThat(actual).extracting(UserTypeDto::id, UserTypeDto::name)
            .containsExactly(expected.id(), expected.name());
    }

    @Test
    void shouldDeleteUserType() {
        UserType userType = new UserType(null, "User", null);
        userTypeRepository.save(userType);
        Long id = userType.getId();

        userTypeService.deleteUserType(id);

        assertThat(userTypeRepository.findById(id)).isEmpty();
    }

}
