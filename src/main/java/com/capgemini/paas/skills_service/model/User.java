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
@Table(name = "USER")
public class User implements ObjectValidator {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="USER_ID")
	private long id;
	
	@Size(max=20)
	@NotNull
	@Column(name="FIRST_NAME", nullable = false)
	private String firstName;

	@Size(max=20)
	@NotNull
	@Column(name="SURNAME", nullable = false)
	private String surname;
}
