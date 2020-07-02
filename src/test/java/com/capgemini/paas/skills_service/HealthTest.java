package com.capgemini.paas.skills_service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SkillApplication.class)
@AutoConfigureMockMvc
public class HealthTest {

	@Autowired
    private TestRestTemplate testRestTemplate;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
    public void testCheckHealth_OK() {
    	
    	ResponseEntity<String> skillResponse = testRestTemplate.getForEntity("/operational/health", String.class);
    	
    	JsonNode root;
		try {
			
			root = mapper.readTree(skillResponse.getBody());
			
			assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
			assertEquals("UP", root.path("status").asText());
			
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(true, false);
		}
        
    }
    
    @Test
    public void testCheckInfo_OK() {
    	
    	ResponseEntity<String> skillResponse = testRestTemplate.getForEntity("/operational/info", String.class);
    	
    	assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
        
    }
    
//    @Test
//    public void testCheckPrometheus_OK() {
//    	
//    	ResponseEntity<String> skillResponse = testRestTemplate.getForEntity("/operational/prometheus", String.class);
//    	
//    	assertEquals(HttpStatus.OK, skillResponse.getStatusCode());
//        
//    }

}
