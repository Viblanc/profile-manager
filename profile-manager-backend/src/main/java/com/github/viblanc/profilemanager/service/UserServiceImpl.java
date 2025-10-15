package com.github.viblanc.profilemanager.service;

import org.springframework.stereotype.Service;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.exception.UserTypeNotFoundException;
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
		
		// check if user type exists, otherwise throw an exception
		userTypeRepository.findByName(userDto.userType()).ifPresentOrElse((userType) -> {
			userType.addUser(user);
			// persisting the user type will persist the user as well thanks to cascading
			userTypeRepository.save(userType);
		}, () -> {
			throw new UserTypeNotFoundException("User type does not exist");
		});
		
		return userDto;
	}
}
