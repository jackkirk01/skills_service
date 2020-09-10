package com.capgemini.paas.skills_service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.capgemini.paas.skills_service.model.validation.ObjectValidator;
import com.capgemini.paas.skills_service.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter @Setter @ToString @NoArgsConstructor @EqualsAndHashCode @AllArgsConstructor @Builder
@DynamicUpdate
@Table(name = "EMPLOYEE")
public class User implements ObjectValidator {
	
	@Id
	@Column(name="USER_ID")
	private long userId;
	
	@Size(max=20)
	@NotNull
	@Column(name="FIRST_NAME", nullable = false)
	private String firstName;

	@Size(max=20)
	@NotNull
	@Column(name="SURNAME", nullable = false)
	private String surname;
	
	@NotNull
	@Column (name="ROLE", nullable = false, insertable = false, updatable = false)
	private Role role;
	
	@Size(max=100)
	@Column (name="LAST_UPDATED_SKILLS")
	private String lastUpdatedSkills;
}
