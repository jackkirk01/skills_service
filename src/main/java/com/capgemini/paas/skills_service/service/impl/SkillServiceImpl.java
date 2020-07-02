package com.capgemini.paas.skills_service.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.paas.services.errorhandling.persistence.DataNotFoundException;
import com.capgemini.paas.skills_service.model.Skill;
import com.capgemini.paas.skills_service.model.SkillUserLink;
import com.capgemini.paas.skills_service.model.User;
import com.capgemini.paas.skills_service.model.dto.SkillDTO;
import com.capgemini.paas.skills_service.model.dto.UserDTO;
import com.capgemini.paas.skills_service.model.enums.Proficiency;
import com.capgemini.paas.skills_service.persistence.dao.SkillRepository;
import com.capgemini.paas.skills_service.persistence.dao.SkillUserLinkRepository;
import com.capgemini.paas.skills_service.service.SkillService;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

	@Autowired
	SkillRepository skillRepository;
	
	@Override
	public List<SkillDTO> getSkills() {
		
		List<SkillDTO> skillsDto = new ArrayList<SkillDTO>();
								
		skillRepository.findAll().forEach(skill -> {
			skillsDto.add(mapToDto(skill));			
		});
		
		return skillsDto;
	}
	
	@Override
	public Optional<SkillDTO> getSkillById(long id) {
		
		Optional<Skill> skill = skillRepository.findById(id);
	
		Optional<SkillDTO> skillDto = Optional.empty();
		
		if(skill.isPresent()) {
			
			skillDto = Optional.of(mapToDto(skill.get()));
		}
		
		return skillDto;
	}
	
	@Override
	public SkillDTO saveSkill(Skill skill) {
				
		return mapToDto(skillRepository.save(skill));
	}

	@Override
	public void deleteSkill(long id) {
		skillRepository.deleteById(id);
	}

	@Override
	public List<UserDTO> getUsersBySkillId(long id, Optional<Proficiency> proficiency) {
				
		Optional<Skill> skill = skillRepository.findById(id);
				
		List<UserDTO> usersDto = new ArrayList<UserDTO>();
		
		if(!skill.isPresent()) {
			throw new DataNotFoundException("Skill not found for ID: " + id);			
		} else {
			
			skill.get().getSkillUserLink().forEach(link -> 
			{
				User user = link.getUser();
				
				UserDTO userDto = UserDTO.builder()
						.userId(user.getId())
						.firstName(user.getFirstName())
						.surname(user.getSurname())
						.proficiency(link.getProficiency())
						.build();
				
				if(proficiency.isPresent()) {
					if (link.getProficiency().equals(proficiency.get())) {
						usersDto.add(userDto);				
					}
				} else {
					usersDto.add(userDto);
				}
			});
		}
			
		return usersDto;
	}
	
	public SkillDTO mapToDto (Skill skill) {
				
		List<UserDTO> usersDto = new ArrayList<UserDTO>();
		
		if(skill.getSkillUserLink() != null) {
		
			skill.getSkillUserLink().forEach(link -> {
		
				User user = link.getUser();
				
				usersDto.add(UserDTO.builder()
						.userId(user.getId())
						.firstName(user.getFirstName())
						.surname(user.getSurname())
						.proficiency(link.getProficiency())
						.build());
				
			});
		}
		
		SkillDTO skillDto = SkillDTO.builder()
				.skillId(skill.getId())
				.name(skill.getName())
				.priority(skill.getPriority())
				.type(skill.getType())
				.users(usersDto)
				.build();
		
		return skillDto;
	
	}
}
