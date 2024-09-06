package com.techlabs.app.repository;

import com.techlabs.app.entity.InsurancePolicy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

//	List<InsurancePolicy> findByCustomersId(Long customerId);
	
}