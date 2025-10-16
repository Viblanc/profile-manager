package com.github.viblanc.profilemanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers() {
		return ResponseEntity.ok(userService.findAll());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUser(id));
	}
	
	@PostMapping
	public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
		UserDto addedUser = userService.addUser(userDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<UserDto> editUser(@PathVariable Long id, @RequestBody UserDto userDto) {
		UserDto updatedUser = userService.editUser(id, userDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.removeUser(id);
		
		return ResponseEntity.noContent().build();
	}
}
