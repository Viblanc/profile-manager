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

import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.service.UserTypeService;

@RestController
@RequestMapping("/api/user_types")
public class UserTypeController {
	private final UserTypeService userTypeService;
	
	public UserTypeController(UserTypeService userTypeService) {
		this.userTypeService = userTypeService;
	}
	
	@GetMapping
	public ResponseEntity<List<UserTypeDto>> getUserTypes() {
		return ResponseEntity.ok(this.userTypeService.findAll());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<UserTypeDto> getUserType(@PathVariable Long id) {
		return ResponseEntity.ok(this.userTypeService.getUserType(id));
	}
	
	@PostMapping
	public ResponseEntity<UserTypeDto> addUserType(@RequestBody UserTypeDto dto) {
		UserTypeDto newUserType = this.userTypeService.addUserType(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUserType);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<UserTypeDto> editUserType(@PathVariable Long id, @RequestBody UserTypeDto dto) {
		UserTypeDto updatedUserType = this.userTypeService.editUserType(id, dto);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUserType);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteUserType(@PathVariable Long id) {
		this.userTypeService.deleteUserType(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
