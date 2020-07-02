package com.capgemini.paas.skills_service.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.capgemini.paas.skills_service.model.enums.Priority;
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
@Table(name = "SKILL")
public class Skill implements ObjectValidator {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SKILL_ID")
	private long id;
	
	@Size(max=50)
	@NotNull
	@Column(name="NAME", nullable = false)
	private String name;
	
	@Size(max=20)
	@NotNull
	@Column(name="TYPE", nullable = false)
	private String type;
	
	@NotNull
	@Column(name="PRIORITY", nullable = false)
	private Priority priority;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "skill", cascade = CascadeType.REMOVE)
	private List<SkillUserLink> skillUserLink;
	
}
