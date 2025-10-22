package com.github.viblanc.profilemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(Long id,
        @NotBlank(message = "First name cannot be blank.") @Size(max = 64,
                message = "First name cannot exceed 64 characters.") String firstName,
        @NotBlank(message = "Last name cannot be blank.") @Size(max = 64,
                message = "Last name cannot exceed 64 characters.") String lastName,
        @NotBlank(message = "Email cannot be blank.") @Email(message = "Email must be valid.",
                regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$") String email,
        @NotNull(message = "User type cannot be null.") UserTypeDto userType) {

}
