package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>{

}
