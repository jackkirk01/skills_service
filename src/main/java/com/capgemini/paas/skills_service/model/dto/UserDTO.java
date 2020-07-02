package com.capgemini.paas.skills_service.model.dto;

import org.hibernate.annotations.DynamicUpdate;

import com.capgemini.paas.skills_service.model.enums.Proficiency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor @EqualsAndHashCode @AllArgsConstructor @Builder
@DynamicUpdate
public class UserDTO {
	
	private long userId;
	private String firstName;
	private String surname;
	private Proficiency proficiency;
}
