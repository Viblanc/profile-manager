package com.github.viblanc.profilemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.viblanc.profilemanager.dto.ErrorResponse;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleNotFoundException(UserTypeNotFoundException exception) {
		ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleAlreadyExistsException(UserTypeAlreadyExistsException exception) {
		ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleAlreadyExistsException(EmailAlreadyExistsException exception) {
		ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

}
