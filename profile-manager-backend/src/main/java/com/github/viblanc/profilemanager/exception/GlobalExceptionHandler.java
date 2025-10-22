package com.github.viblanc.profilemanager.exception;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.viblanc.profilemanager.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
		logger.warn("Illegal Argument Exception: {}", exception.getMessage());
		ErrorResponse response = new ErrorResponse(Instant.now(), exception.getMessage());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserTypeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(UserTypeNotFoundException exception) {
		logger.warn("User type not found: {}", exception.getMessage());
		ErrorResponse response = new ErrorResponse(Instant.now(), exception.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(UserNotFoundException exception) {
		logger.warn("User not found: {}", exception.getMessage());
		ErrorResponse response = new ErrorResponse(Instant.now(), exception.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserTypeAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyExistsException(UserTypeAlreadyExistsException exception) {
		logger.info("User type already exists: {}", exception.getMessage());
		ErrorResponse response = new ErrorResponse(Instant.now(), exception.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyExistsException(EmailAlreadyExistsException exception) {
		logger.info("Email already exists: {}", exception.getMessage());
		ErrorResponse response = new ErrorResponse(Instant.now(), exception.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
		logger.info("Validation error: {}", exception.getMessage());
		List<String> errors = exception.getBindingResult()
			.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();
		return new ResponseEntity<>(new ErrorResponse(Instant.now(), "Validation Failed", errors),
				HttpStatus.BAD_REQUEST);
	}

}
