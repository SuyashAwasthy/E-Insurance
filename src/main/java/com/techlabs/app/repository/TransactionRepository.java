package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.TaxSetting;
import com.techlabs.app.entity.Transaction;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	List<Transaction> findAllByAgentAndType(Agent agent, String type);

}
