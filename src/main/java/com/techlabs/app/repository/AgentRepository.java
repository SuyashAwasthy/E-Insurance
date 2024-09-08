package com.techlabs.app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>{

	List<Agent> findAgentsByRegistrationDateBetween(LocalDateTime startDateTime,
			LocalDateTime endDateTime);

	List<Agent> findAgentsByRegistrationDateBetween(LocalDate startDateTime, LocalDate endDateTime);

}
