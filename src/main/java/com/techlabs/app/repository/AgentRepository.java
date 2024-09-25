package com.techlabs.app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>{

	List<Agent> findAgentsByRegistrationDateBetween(LocalDateTime startDateTime,
			LocalDateTime endDateTime);

	List<Agent> findAgentsByRegistrationDateBetween(LocalDate startDateTime, LocalDate endDateTime);

	Agent findByUserUsername(String username);

	Optional<Agent> findByUserId(Long userId);

	 Optional<Agent> findByUser(User user);
	
	//Agent findByUser(User user);

	
	List<Agent> findByIsActive(boolean active);

	Page<Agent> findByIsActive(boolean active, Pageable pageable);

	//Optional<User> findByUserDetails(User user);

	//Optional<Role> findByUser_FirstNameAndUser_LastName(String name);

	//Optional<Role> findByUser_FirstNameAndUser_LastName(String firstName, String lastName);
	
	@Query("SELECT COUNT(a) FROM Agent a WHERE a.isActive = true") 
    int countActiveAgents();
	
	
}
