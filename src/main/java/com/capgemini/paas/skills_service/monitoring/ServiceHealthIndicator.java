package com.capgemini.paas.skills_service.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.capgemini.paas.skills_service.persistence.dao.SkillRepository;

@Component
public class ServiceHealthIndicator implements HealthIndicator {
	
	@Autowired
	SkillRepository skillRepository;
	
	@Override
    public Health health() {
		
		boolean isDown = false;
        
		try {
    		if (skillRepository.findAll() == null) {
    			isDown = true;
    		} else {
    			isDown = false;
    		}
    	} catch (Exception ex) {
    		isDown = true;
    	}
		
        return isDown ? Health.down().withDetail("ERROR", "Database cannot be detected").build() : Health.up().build();
        
    }
 
}