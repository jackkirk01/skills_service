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
import org.springframework.http.HttpEntity;
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
import com.capgemini.paas.skills_service.model.dto.SkillDTO;
import com.capgemini.paas.skills_service.model.dto.UserDTO;
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
	
	private List<SkillDTO> expectedSkills = new ArrayList<>();
	private ObjectMapper mapper = new ObjectMapper();
	
	private SkillDTO badSkillRequest;
	
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
		
		User user1 = userRepository.save(User.builder()
				.firstName("Jack")
				.surname("Kirk")
				.build());
		
		User user2 = userRepository.save(User.builder()
				.firstName("James")
				.surname("Neate")
				.build());
		
		skillUserLinkRepository.save(SkillUserLink.builder()
				.skillUserLinkId(SkillUserLinkId.builder()
						.skillId(skill1.getSkillId())
						.userId(user1.getUserId())
						.build())
				.proficiency(Proficiency.EXPERIENCED)
				.user(user1)
				.build());
		
		skillUserLinkRepository.save(SkillUserLink.builder()
				.skillUserLinkId(SkillUserLinkId.builder()
						.skillId(skill2.getSkillId())
						.userId(user2.getUserId())
						.build())
				.proficiency(Proficiency.EXPERIENCED)
				.user(user2)
				.build());
		
		skillRepository.findAll().forEach(skill -> {
			expectedSkills.add(mapToDto(skill));			
		});
		
		this.badSkillRequest = SkillDTO.builder()
				.skillId(9999)
				.name(null)
				.priority(null)
				.type(null)
				.users(null)
				.build();

								
	}
	
	@Test
	public void testCreateSkill_OK () throws IOException {
		
		SkillDTO expectedSkill = expectedSkills.get(0);
		expectedSkill.setUsers(new ArrayList<UserDTO>());
				
		HttpEntity<SkillDTO> request = new HttpEntity<>(expectedSkill);
		
		ResponseEntity<SkillDTO> skillResponse = testRestTemplate.postForEntity("/skills-tracker/v1/skills/", request, SkillDTO.class);
										
		assertEquals(HttpStatus.CREATED, skillResponse.getStatusCode());
		assertEquals(expectedSkill, skillResponse.getBody());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, skillResponse.getHeaders().getContentType());
	}
	
	@Test
	public void testCreateSkill_BAD_REQUEST () throws IOException {
				
		HttpEntity<SkillDTO> request = new HttpEntity<>(badSkillRequest);
		
		ResponseEntity<String> skillResponse = testRestTemplate.postForEntity("/skills-tracker/v1/skills/", request, String.class);
				
		JsonNode root = mapper.readTree(skillResponse.getBody());
						
		assertEquals(HttpStatus.BAD_REQUEST, skillResponse.getStatusCode());
		assertEquals(400, root.path("statusCode").asInt());
	}
	
	@Test
	public void testGetAllSkills_OK() {
		
		ResponseEntity<List<SkillDTO>> skillResponse = testRestTemplate.exchange("/skills-tracker/v1/skills/", HttpMethod.GET, null, new ParameterizedTypeReference<List<SkillDTO>>(){});
		
		assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, skillResponse.getHeaders().getContentType());
		assertEquals(expectedSkills, skillResponse.getBody());
		
	}
	
	@Test
	public void testGetAllSkills_NOT_FOUND() throws IOException {
		
		tearDown();
		
		ResponseEntity<String> skillResponse = testRestTemplate.getForEntity("/skills-tracker/v1/skills/", String.class);
		
		JsonNode root = mapper.readTree(skillResponse.getBody());
		
		assertEquals(HttpStatus.NOT_FOUND, skillResponse.getStatusCode());
		assertEquals("No skills exist", root.path("message").asText());
		assertEquals(404, root.path("statusCode").asInt());
		
	}
	
	@Test
	public void testGetSkillById_OK() {
		
		SkillDTO expectedSkill = expectedSkills.get(0);
		long id = expectedSkill.getSkillId();
		
		ResponseEntity<SkillDTO> skillResponse = testRestTemplate.getForEntity("/skills-tracker/v1/skills/" + id, SkillDTO.class);
		
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
	
	@Test
	public void testUpdateSkillById_OK () throws IOException {
		
		SkillDTO expectedSkill = expectedSkills.get(0);
		long id = expectedSkill.getSkillId();
		expectedSkill.setName("newName");
		expectedSkill.setUsers(new ArrayList<UserDTO>());
		HttpEntity<SkillDTO> request = new HttpEntity<>(expectedSkill);
		
		ResponseEntity<SkillDTO> skillResponse = testRestTemplate.exchange("/skills-tracker/v1/skills/" + id, HttpMethod.PUT, request, SkillDTO.class);
				
		assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, skillResponse.getHeaders().getContentType());
		assertEquals(expectedSkill, skillResponse.getBody());
	}
	
	@Test
	public void testUpdateSkillById_BAD_REQUEST () throws IOException {
		
		long id = badSkillRequest.getSkillId();
		
		HttpEntity<SkillDTO> request = new HttpEntity<>(badSkillRequest);
		
		ResponseEntity<String> skillResponse = testRestTemplate.exchange("/skills-tracker/v1/skills/" + id, HttpMethod.PUT, request, String.class);
		
		JsonNode root = mapper.readTree(skillResponse.getBody());
						
		assertEquals(HttpStatus.BAD_REQUEST, skillResponse.getStatusCode());
		assertEquals(400, root.path("statusCode").asInt());
	}
	
	@Test
	public void testUpdateSkillById_NOT_FOUND () throws IOException {
		SkillDTO expectedSkill = expectedSkills.get(0);
		long id = 99999L;
		expectedSkill.setName("newName");
		expectedSkill.setUsers(new ArrayList<UserDTO>());
		HttpEntity<SkillDTO> request = new HttpEntity<>(expectedSkill);
		
		ResponseEntity<String> skillResponse = testRestTemplate.exchange("/skills-tracker/v1/skills/" + id, HttpMethod.PUT, request, String.class);
		
		JsonNode root = mapper.readTree(skillResponse.getBody());
						
		assertEquals(HttpStatus.NOT_FOUND, skillResponse.getStatusCode());
		assertEquals("Skill not found for ID: " + id, root.path("message").asText());
		assertEquals(404, root.path("statusCode").asInt());
	}
	
	@Test
	public void testDeleteSkillById_OK () throws IOException {
		
		SkillDTO expectedSkill = expectedSkills.get(0);

		long id = expectedSkill.getSkillId();
				
		ResponseEntity<String> skillResponse = testRestTemplate.exchange("/skills-tracker/v1/skills/" + id, HttpMethod.DELETE, null, String.class);
		
		//Check status code 200, OK
		assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
		
		ResponseEntity<String> getResponse = testRestTemplate.getForEntity("/skills-tracker/v1/skills/" + id, String.class);
		
		JsonNode root = mapper.readTree(getResponse.getBody());
		
		//Check Skill was deleted
		assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
		assertEquals("Skill not found for ID: " + id, root.path("message").asText());
		assertEquals(404, root.path("statusCode").asInt());
		
	}
	
	@Test
	public void testDeleteSkillById_NOT_FOUND () throws IOException {
		
		long id = 99999L;
				
		ResponseEntity<String> skillResponse = testRestTemplate.exchange("/skills-tracker/v1/skills/" + id, HttpMethod.DELETE, null, String.class);
				
		JsonNode root = mapper.readTree(skillResponse.getBody());
		
		assertEquals(HttpStatus.NOT_FOUND, skillResponse.getStatusCode());
		assertEquals("Skill not found for ID: " + id, root.path("message").asText());
		assertEquals(404, root.path("statusCode").asInt());
		
	}
	
	@After
	public void tearDown() {
		skillUserLinkRepository.deleteAll();
		userRepository.deleteAll();
		skillRepository.deleteAll();
		expectedSkills.clear();
	}
	
	public SkillDTO mapToDto (Skill skill) {
		
		List<UserDTO> usersDto = new ArrayList<UserDTO>();
		
		if(skill.getSkillUserLink() != null) {
		
			skill.getSkillUserLink().forEach(link -> {
		
				User user = link.getUser();
				
				usersDto.add(UserDTO.builder()
						.userId(user.getUserId())
						.firstName(user.getFirstName())
						.surname(user.getSurname())
						.proficiency(link.getProficiency())
						.build());
				
			});
		}
		
		SkillDTO skillDto = SkillDTO.builder()
				.skillId(skill.getSkillId())
				.name(skill.getName())
				.priority(skill.getPriority())
				.type(skill.getType())
				.users(usersDto)
				.build();
		
		return skillDto;
	
	}
	
}