package com.github.viblanc.profilemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.viblanc.profilemanager.dto.UserDto;
import com.github.viblanc.profilemanager.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
		UserDto addedUser = userService.addUser(userDto);
		
		return new ResponseEntity<>(addedUser, HttpStatus.OK);
	}
}
