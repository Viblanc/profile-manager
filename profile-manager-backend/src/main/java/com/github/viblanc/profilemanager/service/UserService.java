package com.github.viblanc.profilemanager.service;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;

public interface UserService {
	User addUser(UserDto userDto);
}
