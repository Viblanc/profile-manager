package com.github.viblanc.profilemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	User toUser(UserDto userDto);
	
	default UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserType().getName());
    }
}
