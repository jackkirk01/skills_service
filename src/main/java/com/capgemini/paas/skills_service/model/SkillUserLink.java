package com.capgemini.paas.skills_service.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.capgemini.paas.skills_service.model.enums.Proficiency;

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
@Table(name = "SKILL_USER_LINK")
public class SkillUserLink {
	
	@EmbeddedId
	private SkillUserLinkId skillUserLinkId;
	
	@ManyToOne
	@MapsId("USER_ID")
	@JoinColumn(name = "USER_ID")
	private User user;
	
	@ManyToOne
	@MapsId("SKILL_ID")
	@JoinColumn(name = "SKILL_ID")
	private Skill skill;
	
	@Column(name="PROFICIENCY", nullable = false)
	private Proficiency proficiency;
	
}
