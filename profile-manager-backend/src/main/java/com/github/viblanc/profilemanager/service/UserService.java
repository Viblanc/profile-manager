package com.github.viblanc.profilemanager.service;

import java.util.List;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;

public interface UserService {
	List<UserDto> findAll();
	User addUser(UserDto userDto);
}
