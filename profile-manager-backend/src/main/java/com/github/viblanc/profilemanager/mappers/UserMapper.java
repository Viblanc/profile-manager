package com.github.viblanc.profilemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "userType.users", ignore = true)
	User toUser(UserDto userDto);

	UserDto toDto(User user);

}
