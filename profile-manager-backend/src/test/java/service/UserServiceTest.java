package service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.mappers.UserMapper;
import com.github.viblanc.profilemanager.repository.UserRepository;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;
import com.github.viblanc.profilemanager.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private UserRepository userRepository;

    private static final UserType USER_TYPE = new UserType(1L, "Admin", null);

    private static final UserTypeDto USER_TYPE_DTO = new UserTypeDto(1L, "Admin");

    private static final User USER = new User(1L, "John", "Doe", "john@doe.mail", USER_TYPE);

    private static final UserDto USER_DTO = new UserDto(1L, "John", "Doe", "john@doe.mail", USER_TYPE_DTO);

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(1L, "John", "Doe", "john@doe.mail", USER_TYPE),
                new User(2L, "Jane", "Doe", "jane@doe.mail", USER_TYPE)));

        List<UserDto> expected = List.of(new UserDto(1L, "John", "Doe", "john@doe.mail", USER_TYPE_DTO),
                new UserDto(2L, "Jane", "Doe", "jane@doe.mail", USER_TYPE_DTO));
        List<UserDto> actual = userService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testGetUser() {
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));

        UserDto actual = userService.getUser(USER_DTO.id());

        assertEquals(USER_DTO, actual);
    }

    @Test
    void testAddUser() {
        when(userTypeRepository.findByName(anyString())).thenReturn(Optional.of(USER_TYPE));
        when(userRepository.save(any(User.class))).thenReturn(USER);

        UserDto actual = userService.addUser(USER_DTO);

        assertEquals(USER_DTO, actual);
    }

    @Test
    void testUpdateUserType() {
        when(userTypeRepository.findByName(anyString())).thenReturn(Optional.of(USER_TYPE));
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(USER);

        UserDto actual = userService.editUser(USER.getId(), USER_DTO);

        assertEquals(USER_DTO, actual);
    }

    @Test
    void testDeleteUserType() {
        userService.removeUser(USER.getId());

        verify(userRepository).deleteById(USER.getId());
    }

}
