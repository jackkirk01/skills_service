package com.capgemini.paas.skills_service.exception;

public class EntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EntityException() {
		super();
	}

	public EntityException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EntityException(String message) {
		super(message);
	}

	public EntityException(Throwable cause) {
		super(cause);
	}

}
