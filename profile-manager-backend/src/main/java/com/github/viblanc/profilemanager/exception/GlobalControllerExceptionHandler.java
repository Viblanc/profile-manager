package com.github.viblanc.profilemanager.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.viblanc.profilemanager.dto.ErrorResponse;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(UserTypeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(UserTypeNotFoundException exception) {
		ErrorResponse response = new ErrorResponse(exception.getMessage(), System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserTypeAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyExistsException(UserTypeAlreadyExistsException exception) {
		ErrorResponse response = new ErrorResponse(exception.getMessage(), System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyExistsException(EmailAlreadyExistsException exception) {
		ErrorResponse response = new ErrorResponse(exception.getMessage(), System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleArgumentNotValid(MethodArgumentNotValidException exception) {
		Map<String, String> errors = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

		return ResponseEntity.badRequest().body(errors);
	}

}
