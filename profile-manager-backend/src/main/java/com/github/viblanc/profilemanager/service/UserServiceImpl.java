package com.github.viblanc.profilemanager.service;

import org.springframework.stereotype.Service;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.exception.EmailAlreadyExistsException;
import com.github.viblanc.profilemanager.exception.UserTypeNotFoundException;
import com.github.viblanc.profilemanager.repository.UserRepository;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserTypeRepository userTypeRepository;

	public UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository) {
		this.userRepository = userRepository;
		this.userTypeRepository = userTypeRepository;
	}

	@Override
	public User addUser(UserDto userDto) {
		// throw exception if user with same email address already exists
		userRepository.findByEmail(userDto.email()).ifPresent(u -> {
			throw new EmailAlreadyExistsException("User with email " + userDto.email() + " already exists.");
		});
		
		// create user from dto
		User user = User.build().firstName(userDto.firstName()).lastName(userDto.lastName()).email(userDto.email())
				.build();

		// retrieve user type, throw exception if it does not exist
		UserType userType = userTypeRepository.findByName(userDto.userType()).orElseThrow(
				() -> new UserTypeNotFoundException("User type " + userDto.userType() + " does not exist"));
		
		// add user to the user type
		userType.addUser(user);
		
		// persist the new user and the updated user type
		userRepository.save(user);
		userTypeRepository.save(userType);
		
		return user;
	}
}
