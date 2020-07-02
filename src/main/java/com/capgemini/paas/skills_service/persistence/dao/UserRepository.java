package com.capgemini.paas.skills_service.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.paas.skills_service.model.Skill;
import com.capgemini.paas.skills_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	// Default JPA Repository methods exist via inheritance i.e. save
	// Custom query methods can be created for any field in the Object i.e. Skill
	
//	List<Skill> findBySkillName(String skillName);
	
	// Table 4 contains a useful reference of all supported methods for JPA that extend the default Repository
	// https://docs.spring.io/spring-data/data-jpa/docs/2.0.4.RELEASE/reference/html/#repositories.query-methods.query-creation
	
}