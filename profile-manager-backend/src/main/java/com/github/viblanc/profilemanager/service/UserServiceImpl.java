package com.github.viblanc.profilemanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.exception.EmailAlreadyExistsException;
import com.github.viblanc.profilemanager.exception.UserNotFoundException;
import com.github.viblanc.profilemanager.exception.UserTypeNotFoundException;
import com.github.viblanc.profilemanager.mappers.UserMapper;
import com.github.viblanc.profilemanager.repository.UserRepository;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final UserTypeRepository userTypeRepository;

	private final UserMapper mapper;

	public UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository, UserMapper mapper) {
		this.userRepository = userRepository;
		this.userTypeRepository = userTypeRepository;
		this.mapper = mapper;
	}

	@Override
	public List<UserDto> findAll() {
		List<User> users = userRepository.findAll();

		return users.stream().map(mapper::toDto).toList();
	}

	@Override
	public UserDto getUser(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

		return mapper.toDto(user);
	}

	@Override
	public UserDto addUser(UserDto userDto) {
		// throw exception if user with same email address already exists
		userRepository.findByEmail(userDto.email()).ifPresent(u -> {
			throw new EmailAlreadyExistsException("User with email " + userDto.email() + " already exists.");
		});

		// retrieve user type, throw exception if it does not exist
		UserType userType = userTypeRepository.findByName(userDto.userType().name())
			.orElseThrow(() -> new UserTypeNotFoundException("User type " + userDto.userType().name() + " does not exist"));

		// create user from dto
		User user = mapper.toUser(userDto);

		// add user to the user type
		userType.addUser(user);

		// persist the new user and the updated user type
		userRepository.save(user);
		userTypeRepository.save(userType);

		return mapper.toDto(user);
	}

	@Override
	public UserDto editUser(Long id, UserDto userDto) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

		// check if email is available
		if (!user.getEmail().equals(userDto.email()) && userRepository.findByEmail(userDto.email()).isPresent()) {
			throw new EmailAlreadyExistsException("User with email " + userDto.email() + " already exists.");
		}

		// check if user type exists
		UserType userType = userTypeRepository.findByName(userDto.userType().name())
			.orElseThrow(() -> new UserTypeNotFoundException("User type " + userDto.userType().name() + " does not exist"));

		// replace old data with new data
		user.setFirstName(userDto.firstName());
		user.setLastName(userDto.lastName());
		user.setEmail(userDto.email());
		userType.addUser(user);

		userTypeRepository.save(userType);
		userRepository.save(user);

		return mapper.toDto(user);
	}

	@Override
	public void removeUser(Long id) {
		userRepository.deleteById(id);
	}

}
