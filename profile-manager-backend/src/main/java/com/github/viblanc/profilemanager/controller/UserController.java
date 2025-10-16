package com.github.viblanc.profilemanager.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.entity.User;
import com.github.viblanc.profilemanager.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		return ResponseEntity.ok(userService.findAll());
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
		User addedUser = userService.addUser(userDto);
		EntityModel<User> resource = EntityModel.of(addedUser);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(resource);
	}
}
