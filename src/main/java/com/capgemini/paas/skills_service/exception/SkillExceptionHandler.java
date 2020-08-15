package com.capgemini.paas.skills_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.logstash.logback.encoder.org.apache.commons.lang.exception.ExceptionUtils;

@ControllerAdvice
public class SkillExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SkillExceptionHandler.class);

	public SkillExceptionHandler() {
		super();
	}
	
	@ExceptionHandler(value = {DataNotFoundException.class})
	protected ResponseEntity<ServiceException> handlePersistenceNoData(DataNotFoundException ex, WebRequest request) {
		
		// Business specific logic goes here
		return new ResponseEntity<>(generateServiceException(HttpStatus.NOT_FOUND, ex), HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(value = {BadRequestException.class})
	protected ResponseEntity<ServiceException> handleBadRequest(BadRequestException ex, WebRequest request) {

		// Business specific logic goes here
		return new ResponseEntity<>(generateServiceException(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(value = {EntityException.class})
	protected ResponseEntity<ServiceException> handlePersistenceEntityException(EntityException ex, WebRequest request) {

		// Business specific logic goes here
		return new ResponseEntity<>(generateServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ex), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(value = {NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
	protected ResponseEntity<ServiceException> handleInternalServerError(RuntimeException ex, WebRequest request) {
		
		String message = "Unexpected error occured. Please try again later or contact your system administrator if the issue persists.";

		LOGGER.error("ERROR - (" + message + ") (" + ExceptionUtils.getRootCauseMessage(ex) + "): " + ex.toString(), ex);
		
		return new ResponseEntity<>(new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), message), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	private ServiceException generateServiceException(HttpStatus httpStatus, Exception ex) {
		
        String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        
        LOGGER.error("ERROR - (" + message + ") (" + ExceptionUtils.getRootCauseMessage(ex) + "): " + ex.toString(), ex);
        
        return new ServiceException(httpStatus.value(), message);
        
    }
	
}