package com.capgemini.paas.skills_service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.capgemini.paas.skills_service.model.Skill;
import com.capgemini.paas.skills_service.model.SkillUserLink;
import com.capgemini.paas.skills_service.model.SkillUserLinkId;
import com.capgemini.paas.skills_service.model.User;
import com.capgemini.paas.skills_service.model.enums.Priority;
import com.capgemini.paas.skills_service.model.enums.Proficiency;
import com.capgemini.paas.skills_service.persistence.dao.SkillRepository;
import com.capgemini.paas.skills_service.persistence.dao.SkillUserLinkRepository;
import com.capgemini.paas.skills_service.persistence.dao.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SkillApplication.class)
@AutoConfigureMockMvc 
public class SkillTest {
	
	@Autowired
	private SkillRepository skillRepository;
	
	@Autowired
	private SkillUserLinkRepository skillUserLinkRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private TestRestTemplate testRestTemplate;
	
	private List<Skill> expectedSkills = new ArrayList<>();
	private ObjectMapper mapper = new ObjectMapper();
	
	@Before
	public void init() {
		
		Skill skill1 = skillRepository.save(Skill.builder()
				.name("NAME1")
				.priority(Priority.HIGH)
				.type("type")
				.build());
		
		Skill skill2 = skillRepository.save(Skill.builder()
				.name("NAME2")
				.priority(Priority.MEDIUM)
				.type("type")				
				.build());
		
		Skill skill3 = skillRepository.save(Skill.builder()
				.name("NAME3")
				.priority(Priority.LOW)
				.type("type")
				.build());
		
		List<SkillUserLink> links1 = List.of(
				skillUserLinkRepository.save(SkillUserLink.builder()
						.skillUserLinkId(SkillUserLinkId.builder()
								.skillId(1L)
								.userId(1L)
								.build())
						.proficiency(Proficiency.EXPERIENCED)
						.user(userRepository.save(User.builder()
								.id(1)
								.firstName("Jack")
								.surname("Kirk")
								.build()))
						.build())
				,
				skillUserLinkRepository.save(SkillUserLink.builder()
						.skillUserLinkId(SkillUserLinkId.builder()
								.skillId(1L)
								.userId(2L)
								.build())
						.proficiency(Proficiency.EXPERT)
						.user(userRepository.save(User.builder()
								.id(2)
								.firstName("James")
								.surname("Neate")
								.build()))
						.build())
				);
		
		List<SkillUserLink> links2 = List.of(
				skillUserLinkRepository.save(SkillUserLink.builder()
						.skillUserLinkId(SkillUserLinkId.builder()
								.skillId(2L)
								.userId(1L)
								.build())
						.proficiency(Proficiency.EXPERIENCED)
						.user(userRepository.save(User.builder()
								.id(1)
								.firstName("Jack")
								.surname("Kirk")
								.build()))
						.build())
				,
				skillUserLinkRepository.save(SkillUserLink.builder()
						.skillUserLinkId(SkillUserLinkId.builder()
								.skillId(2L)
								.userId(2L)
								.build())
						.proficiency(Proficiency.EXPERT)
						.user(userRepository.save(User.builder()
								.id(2)
								.firstName("James")
								.surname("Neate")
								.build()))
						.build())
				);
		
		List<SkillUserLink> links3 = List.of(
				skillUserLinkRepository.save(SkillUserLink.builder()
						.skillUserLinkId(SkillUserLinkId.builder()
								.skillId(3L)
								.userId(1L)
								.build())
						.proficiency(Proficiency.EXPERIENCED)
						.user(userRepository.save(User.builder()
								.id(1)
								.firstName("Jack")
								.surname("Kirk")
								.build()))
						.build())
				,
				skillUserLinkRepository.save(SkillUserLink.builder()
						.skillUserLinkId(SkillUserLinkId.builder()
								.skillId(3L)
								.userId(2L)
								.build())
						.proficiency(Proficiency.EXPERT)
						.user(userRepository.save(User.builder()
								.id(2)
								.firstName("James")
								.surname("Neate")
								.build()))
						.build())
				);
		
		skill1.setSkillUserLink(links1);
		skill2.setSkillUserLink(links2);
		skill3.setSkillUserLink(links3);
		
		skillRepository.save(skill1);
		skillRepository.save(skill2);
		skillRepository.save(skill3);
		
		expectedSkills.addAll(skillRepository.findAll());
		
		assertEquals(3, expectedSkills.size());
		
	}
	
	@Test
	public void testGetAllSkills_OK() {
		
		ResponseEntity<List<Skill>> skillResponse = testRestTemplate.exchange("/skills-tracker/v1/skills/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Skill>>(){});
		
		assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, skillResponse.getHeaders().getContentType());
		assertEquals(expectedSkills, skillResponse.getBody());
		
	}
	
	@Test
	public void testGetAllSkills_NOT_FOUND() throws IOException {
		
		skillRepository.deleteAll();
		
		ResponseEntity<String> skillResponse = testRestTemplate.getForEntity("/skills-tracker/v1/skills/", String.class);
		
		JsonNode root = mapper.readTree(skillResponse.getBody());
		
		assertEquals(HttpStatus.NOT_FOUND, skillResponse.getStatusCode());
		assertEquals("No skills exist", root.path("message").asText());
		assertEquals(404, root.path("statusCode").asInt());
		
	}
	
	@Test
	public void testGetSkillById_OK() {
		
		Skill expectedSkill = expectedSkills.get(0);
		long id = expectedSkill.getId();
		
		ResponseEntity<Skill> skillResponse = testRestTemplate.getForEntity("/skills-tracker/v1/skills/" + id, Skill.class);
		
		assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, skillResponse.getHeaders().getContentType());
		assertEquals(expectedSkill, skillResponse.getBody());
		
	}
	
	@Test
	public void testGetSkillById_NOT_FOUND() throws IOException {
		
		long id = 99999L;
		
		ResponseEntity<String> skillResponse = testRestTemplate.getForEntity("/skills-tracker/v1/skills/" + id, String.class);
		
		JsonNode root = mapper.readTree(skillResponse.getBody());
		
		assertEquals(HttpStatus.NOT_FOUND, skillResponse.getStatusCode());
		assertEquals("Skill not found for ID: " + id, root.path("message").asText());
		assertEquals(404, root.path("statusCode").asInt());
		
	}
	
	@After
	public void tearDown() {
		skillRepository.deleteAll();
	}
	
}