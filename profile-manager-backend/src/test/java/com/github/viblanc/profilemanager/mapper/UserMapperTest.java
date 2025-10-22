package com.github.viblanc.profilemanager.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.mappers.UserMapper;

class UserMapperTest {
	
	private final UserMapper mapper = UserMapper.INSTANCE;
	private final UserType userType = new UserType(1L, "Admin", null);
	private final UserTypeDto userTypeDto = new UserTypeDto(1L, "Admin");
	
	@Test
	void shouldMapUserToUserDto() {
		User user = new User(1L, "John", "Doe", "john@doe.mail", userType);
		
		UserDto userDto = mapper.toDto(user);
		
		assertThat(userDto.id()).isEqualTo(user.getId());
		assertThat(userDto.firstName()).isEqualTo(user.getFirstName());
		assertThat(userDto.lastName()).isEqualTo(user.getLastName());
		assertThat(userDto.email()).isEqualTo(user.getEmail());
		assertThat(userDto.userType()).isEqualTo(userTypeDto);
	}
	
	@Test
	void shouldMapUserDtoToUser() {
		UserDto userDto = new UserDto(1L, "John", "Doe", "john@doe.mail", userTypeDto);
		
		User user = mapper.toUser(userDto);
		
		assertThat(user.getId()).isEqualTo(userDto.id());
		assertThat(user.getFirstName()).isEqualTo(userDto.firstName());
		assertThat(user.getLastName()).isEqualTo(userDto.lastName());
		assertThat(user.getEmail()).isEqualTo(userDto.email());
		assertThat(user.getUserType().getId()).isEqualTo(userType.getId());
		assertThat(user.getUserType().getName()).isEqualTo(userType.getName());
	}
	
}
