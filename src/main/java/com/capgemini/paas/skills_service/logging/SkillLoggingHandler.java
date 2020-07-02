package com.capgemini.paas.skills_service.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Aspect
@Component
@EnableAspectJAutoProxy
public class SkillLoggingHandler {
	
	// You can implement custom logging behaviour here using the CommonLogging PointCuts
	private static final Logger LOGGER = LoggerFactory.getLogger(SkillLoggingHandler.class);
	private ObjectMapper mapper = new ObjectMapper();
	
	// A list of available PointCuts are located at https://github.com/CapgeminiUK-ODU/springboot-service-generator/tree/master/common-logging-service
	// Use either @Before or @AfterReturning annotations to specify before or after a method invocation
	
	@Before("com.capgemini.paas.services.commonlogging.JoinPointConfig.webControllerExecutionPoint()")
	public void webControllerRequestLogging(JoinPoint joint) throws JsonProcessingException {
		
		logRequestMessage(joint);

	}
	
	@AfterReturning(pointcut = "com.capgemini.paas.services.commonlogging.JoinPointConfig.webControllerExecutionPoint()", returning = "result")
	public void webControllerResponseLogging(JoinPoint joint, Object result) throws JsonProcessingException {
		
		logResponseMessage(joint, result);
		
	}
	
	@Before("com.capgemini.paas.services.commonlogging.JoinPointConfig.serviceImplExecutionPoint()")
	public void serviceImplRequestLogging(JoinPoint joint) throws JsonProcessingException {
		
		logRequestMessage(joint);

	}
	
	@AfterReturning(pointcut = "com.capgemini.paas.services.commonlogging.JoinPointConfig.serviceImplExecutionPoint()", returning = "result")
	public void serviceImplResponseLogging(JoinPoint joint, Object result) throws JsonProcessingException {
		
		logResponseMessage(joint, result);
		
	}
	
	@Before("com.capgemini.paas.services.commonlogging.JoinPointConfig.persistenceDaoExecutionPoint()")
	public void persistenceDaoRequestLogging(JoinPoint joint) throws JsonProcessingException {
		
		logRequestMessage(joint);

	}
	
	@AfterReturning(pointcut = "com.capgemini.paas.services.commonlogging.JoinPointConfig.persistenceDaoExecutionPoint()", returning = "result")
	public void persistenceDaoResponseLogging(JoinPoint joint, Object result) throws JsonProcessingException {
		
		logResponseMessage(joint, result);
		
	}
	
	private void logRequestMessage(JoinPoint joint) throws JsonProcessingException {
		
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		if (LOGGER.isDebugEnabled()) {
			
			LOGGER.debug(String.format("Start of - %s method", joint.getStaticPart().getSignature().getName()));
			LOGGER.debug(String.format("Request -:%n %s", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(joint.getArgs())));
			
		}
		
	}
	
	private void logResponseMessage(JoinPoint joint, Object result) throws JsonProcessingException {
		
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		if (LOGGER.isDebugEnabled()) {
			
			LOGGER.debug(String.format("Response -: %n %s", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(null != result ? result : "")));
			LOGGER.debug(String.format("End of  - %s method", joint.getStaticPart().getSignature().getName()));
			
		}
		
	}
	
}