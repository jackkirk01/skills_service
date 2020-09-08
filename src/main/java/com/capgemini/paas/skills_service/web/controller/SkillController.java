package com.capgemini.paas.skills_service.web.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.paas.skills_service.exception.BadRequestException;
import com.capgemini.paas.skills_service.exception.DataNotFoundException;
import com.capgemini.paas.skills_service.model.Skill;
import com.capgemini.paas.skills_service.model.dto.SkillDTO;
import com.capgemini.paas.skills_service.model.dto.UserDTO;
import com.capgemini.paas.skills_service.model.enums.Proficiency;
import com.capgemini.paas.skills_service.persistence.dao.SkillRepository;
import com.capgemini.paas.skills_service.persistence.dao.SkillUserLinkRepository;
import com.capgemini.paas.skills_service.persistence.dao.UserRepository;
import com.capgemini.paas.skills_service.service.SkillService;

@RestController
@RequestMapping("/skills-tracker/v1/skills")
public class SkillController {
	
	@Autowired
	SkillService skillService;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	SkillRepository skillRepository;
	
	@Autowired
	SkillUserLinkRepository skillUserLinkRepository;
	
	public SkillController() {
	     super();
	}
	
	@CrossOrigin(origins = "http://localhost:8000")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<SkillDTO>> retrieveSkills() {
		
		List<SkillDTO> skills = skillService.getSkills();
		
		if (skills.size() == 0) {
			throw new DataNotFoundException("No skills exist");
		} else {
			return new ResponseEntity<List<SkillDTO>>(skills, HttpStatus.OK);
		}
		
	}
	
	@CrossOrigin(origins = "http://localhost:8000")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<SkillDTO> createSkill(@RequestBody Skill skill) {
		
		if(!skill.validate()) {
			throw new BadRequestException("Invalid input object.");
		}
		
		skillService.saveSkill(skill);
		
//		skill.setSkillUserLink(List.of(
//				skillUserLinkRepository.save(SkillUserLink.builder()
//						.skillUserLinkId(SkillUserLinkId.builder()
//								.skillId(1L)
//								.userId(1L)
//								.build())
//						.proficiency(Proficiency.EXPERIENCED)
//						.user(userRepository.save(User.builder()
//								.id(1)
//								.firstName("Jack")
//								.surname("Kirk")
//								.build()))
//						.build())
//				,
//				skillUserLinkRepository.save(SkillUserLink.builder()
//						.skillUserLinkId(SkillUserLinkId.builder()
//								.skillId(1L)
//								.userId(2L)
//								.build())
//						.proficiency(Proficiency.EXPERT)
//						.user(userRepository.save(User.builder()
//								.id(2)
//								.firstName("James")
//								.surname("Neate")
//								.build()))
//						.build())
//				));
						
//		
//		if (skillDto.equals(null)) {
//			throw new DataNotFoundException("No skills exist");
//		} else {
			return new ResponseEntity<SkillDTO>(skillService.saveSkill(skill), HttpStatus.CREATED);
//		}
	
	}
	
	@CrossOrigin(origins = "http://localhost:8000")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<SkillDTO> updateSkill(@RequestBody Skill skill, @PathVariable("id") long id) {
		
		if(!skill.validate()) {
			throw new BadRequestException();
		}
		
		Optional<SkillDTO> skillDto = skillService.getSkillById(id);
		
		if (!skillDto.isPresent()) {
			throw new DataNotFoundException("Skill not found for ID: " + id);
		} else {
			return new ResponseEntity<SkillDTO>(skillService.saveSkill(skill), HttpStatus.OK);
		}
		
	}
	
	@CrossOrigin(origins = "http://localhost:8000")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Optional<SkillDTO>> retrieveSkillBasedOnId(@PathVariable("id") long id) {
		
		Optional<SkillDTO> skillDto = skillService.getSkillById(id);
		
		if (!skillDto.isPresent()) {
			throw new DataNotFoundException("Skill not found for ID: " + id);
		} else {
			return new ResponseEntity<Optional<SkillDTO>>(skillDto, HttpStatus.OK);
		}
		
	}
	
	@CrossOrigin(origins = "http://localhost:8000")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public HttpStatus deleteSkill(@PathVariable("id") long id) {
		
		Optional<SkillDTO> skillDto = skillService.getSkillById(id);
		
		if (!skillDto.isPresent()) {
			throw new DataNotFoundException("Skill not found for ID: " + id);
		} else {
			skillService.deleteSkill(id);
			return HttpStatus.OK;
		}
		
	}
	
	@CrossOrigin(origins = "http://localhost:8000")
	@RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UserDTO>> getUsersBySkillId(@PathVariable("id") long id, @RequestParam(required = false) Optional<Proficiency> proficiency) {
		
		List<UserDTO> usersDto = skillService.getUsersBySkillId(id, proficiency);
		
		if(proficiency.isPresent() && usersDto.size() == 0) {
			throw new DataNotFoundException("No users exist for ID: " + id + " and proficiency: " + proficiency.get());
		} else if(usersDto.size() == 0) {
			throw new DataNotFoundException("No users exist for ID: " + id);
		} else {
			return new ResponseEntity<List<UserDTO>>(usersDto, HttpStatus.OK);			
		}
		
	}
}
