package com.github.viblanc.profilemanager.service;

import java.util.List;

import com.github.viblanc.profilemanager.dto.UserTypeDto;

public interface UserTypeService {
	List<UserTypeDto> findAll();
	UserTypeDto getUserType(Long id);
	UserTypeDto addUserType(UserTypeDto userTypeDto);
	UserTypeDto editUserType(Long id, UserTypeDto userTypeDto);
	void deleteUserType(Long id);
}
