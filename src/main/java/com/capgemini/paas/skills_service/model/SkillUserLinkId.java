package com.capgemini.paas.skills_service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter @Setter @ToString @NoArgsConstructor @EqualsAndHashCode @AllArgsConstructor @Builder
@DynamicUpdate
public class SkillUserLinkId implements Serializable {
	
	private static final long serialVersionUID = 4096449573256227969L;

	@Size(max=50)
    @NotNull
    @Column(name = "SKILL_ID", nullable = false)
    private Long skillId;

    @Size(max=50)
    @NotNull
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
	
}
