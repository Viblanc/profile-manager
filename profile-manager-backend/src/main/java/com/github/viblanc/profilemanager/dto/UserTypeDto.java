package com.github.viblanc.profilemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserTypeDto(Long id, @NotBlank(message = "User type name cannot be blank.") @Size(max = 64,
		message = "User type name cannot exceed 64 characters.") String name) {

}
