package com.capgemini.paas.skills_service.model.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;

public interface ObjectValidator {

  final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  final Validator validator = factory.getValidator();
  static Logger log = LoggerFactory.getLogger(ObjectValidator.class);

  default boolean validate() {

		Set<ConstraintViolation<ObjectValidator>> violations = validator.validate(this);

		if (violations.size() > 0) {
			violations.parallelStream()
				.forEach(constraint -> log.error("Object fails validation for field {} with constraint {} for value {}", constraint.getPropertyPath(), constraint.getMessage(), constraint.getInvalidValue()));
			return false;
		} else {
			return true;
		}

	}

}