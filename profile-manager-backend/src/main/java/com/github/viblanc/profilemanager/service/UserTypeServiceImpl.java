package com.github.viblanc.profilemanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.exception.UserTypeAlreadyExistsException;
import com.github.viblanc.profilemanager.exception.UserTypeNotFoundException;
import com.github.viblanc.profilemanager.mappers.UserTypeMapper;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@Service
public class UserTypeServiceImpl implements UserTypeService {

	private final UserTypeRepository repository;

	private final UserTypeMapper mapper;

	public UserTypeServiceImpl(UserTypeRepository repository, UserTypeMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<UserTypeDto> findAll() {
		return this.repository.findAll().stream().map(mapper::toDto).toList();
	}

	@Override
	public UserTypeDto getUserType(Long id) {
		UserType userType = repository.findById(id)
			.orElseThrow(() -> new UserTypeNotFoundException("User type not found"));

		return mapper.toDto(userType);
	}

	@Override
	public UserTypeDto addUserType(UserTypeDto userTypeDto) {
		String name = userTypeDto.name();

		// check if a user type with the same name already exists
		if (repository.findByName(name).isPresent()) {
			throw new UserTypeAlreadyExistsException("A user type with the name " + name + " already exists.");
		}
		else {
			UserType newUserType = repository.save(mapper.toUserType(userTypeDto));

			return mapper.toDto(newUserType);
		}
	}

	@Override
	public UserTypeDto editUserType(Long id, UserTypeDto userTypeDto) {
		UserType userType = repository.findById(id)
			.orElseThrow(() -> new UserTypeNotFoundException("User type not found"));

		// check if new name is available
		if (!userType.getName().equals(userTypeDto.name()) && repository.findByName(userTypeDto.name()).isPresent()) {
			throw new UserTypeAlreadyExistsException(
					"A user type with the name " + userTypeDto.name() + " already exists.");
		}

		userType.setName(userTypeDto.name());
		repository.save(userType);

		return mapper.toDto(userType);
	}

	@Override
	public void deleteUserType(Long id) {
		repository.deleteById(id);
	}

}
