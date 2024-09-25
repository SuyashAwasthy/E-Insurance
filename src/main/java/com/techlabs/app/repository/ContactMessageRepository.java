package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.ContactMessage;
import com.techlabs.app.entity.Customer;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long>{

	List<ContactMessage> findByAgentId(Long agentId);

}
