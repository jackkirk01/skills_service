package com.capgemini.paas.skills_service.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class ServiceException extends Exception {
	
	private static final long serialVersionUID = 13215646115L;
	
	//Status Code is used to populate the HTTP status code
	private final int statusCode;
	//Message is used to populate a more meaningful reason of the error. 
	private final String message;
	
	public ServiceException(int statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ServiceException [statusCode=" + statusCode + ", message=" + message + "]";
	}

}
