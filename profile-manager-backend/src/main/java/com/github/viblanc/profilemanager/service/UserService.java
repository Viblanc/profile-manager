package com.github.viblanc.profilemanager.service;

import java.util.List;

import com.github.viblanc.profilemanager.dto.UserDto;

public interface UserService {

	List<UserDto> findAll();

	UserDto getUser(Long id);

	UserDto addUser(UserDto userDto);

	UserDto editUser(Long id, UserDto userDto);

	void removeUser(Long id);

}
