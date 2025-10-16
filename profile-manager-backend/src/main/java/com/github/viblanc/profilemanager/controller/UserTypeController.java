package com.github.viblanc.profilemanager.controller;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.exception.UserTypeAlreadyExistsException;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;

@RepositoryRestController
public class UserTypeController {
	private final UserTypeRepository repository;
	
	public UserTypeController(UserTypeRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping("/user_types")
	public ResponseEntity<?> addUserType(@RequestBody UserType userType) {
		String name = userType.getName();
		
		if (repository.findByName(name).isPresent()) {
			throw new UserTypeAlreadyExistsException("A user type with the same name already exists.");
		} else {
			UserType newUserType = repository.save(userType);
			EntityModel<UserType> resource = EntityModel.of(newUserType);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(resource);
		}
	}
}
