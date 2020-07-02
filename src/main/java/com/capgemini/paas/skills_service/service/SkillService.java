package com.capgemini.paas.skills_service.service;

import java.util.List;
import java.util.Optional;

import com.capgemini.paas.skills_service.model.Skill;
import com.capgemini.paas.skills_service.model.SkillUserLink;
import com.capgemini.paas.skills_service.model.User;
import com.capgemini.paas.skills_service.model.dto.SkillDTO;
import com.capgemini.paas.skills_service.model.dto.UserDTO;
import com.capgemini.paas.skills_service.model.enums.Proficiency;

public interface SkillService {
	
	List<SkillDTO> getSkills();
	
	Optional<SkillDTO> getSkillById(long id);
	
	SkillDTO saveSkill(Skill skill);

	void deleteSkill(long id);

	List<UserDTO> getUsersBySkillId(long id, Optional<Proficiency> proficiency);
	
}