package com.github.viblanc.profilemanager.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ErrorResponse {
	
	private Instant timestamp;
	
	private String message;
	
	public ErrorResponse(Instant timestamp, String message) {
		this.timestamp = timestamp;
		this.message = message;
	}
	
	public ErrorResponse(Instant timestamp, String message, Object details) {
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	@JsonInclude(Include.NON_NULL)
	private Object details;

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

}
