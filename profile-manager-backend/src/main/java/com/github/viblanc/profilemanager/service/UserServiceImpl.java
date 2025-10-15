package com.github.viblanc.profilemanager.service;

import org.springframework.stereotype.Service;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Service
public class UserServiceImpl implements UserService {
	private final UserTypeRepository userTypeRepository;

	public UserServiceImpl(UserTypeRepository userTypeRepository) {
		this.userTypeRepository = userTypeRepository;
	}

	@Override
	public UserDto addUser(UserDto userDto) {
		// create user from dto
		User user = User.build()
				.firstName(userDto.firstName())
				.lastName(userDto.lastName())
				.email(userDto.email())
				.build();
		
		UserType userType = userTypeRepository.findByName(userDto.userType());

		userType.addUser(user);
		userTypeRepository.save(userType);
		
		return userDto;
	}
}
