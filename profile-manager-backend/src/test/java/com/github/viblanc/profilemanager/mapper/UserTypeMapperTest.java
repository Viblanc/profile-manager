package com.github.viblanc.profilemanager.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.mappers.UserTypeMapper;

class UserTypeMapperTest {
	
	private final UserTypeMapper mapper = UserTypeMapper.INSTANCE;
	
	@Test
	void shouldMapUserTypeToUserTypeDto() {
		UserType userType = new UserType(1L, "Admin", null);
		
		UserTypeDto userTypeDto = mapper.toDto(userType);
		
		assertThat(userTypeDto.id()).isEqualTo(userType.getId());
		assertThat(userTypeDto.name()).isEqualTo(userType.getName());
	}
	
	@Test
	void shouldMapUserTypeDtoToUserType() {
		UserTypeDto userTypeDto = new UserTypeDto(1L, "Admin");
		
		UserType userType = mapper.toUserType(userTypeDto);
		
		assertThat(userType.getId()).isEqualTo(userTypeDto.id());
		assertThat(userType.getName()).isEqualTo(userTypeDto.name());
	}
	
}
