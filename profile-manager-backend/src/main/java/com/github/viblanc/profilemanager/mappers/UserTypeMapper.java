package com.github.viblanc.profilemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;

@Mapper(componentModel = "spring")
public interface UserTypeMapper {

	UserTypeMapper INSTANCE = Mappers.getMapper(UserTypeMapper.class);

	UserTypeDto toDto(UserType userType);

	@Mapping(target = "users", ignore = true)
	UserType toUserType(UserTypeDto dto);

}
