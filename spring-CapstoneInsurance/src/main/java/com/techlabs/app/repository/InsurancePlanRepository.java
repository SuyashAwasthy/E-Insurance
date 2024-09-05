package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techlabs.app.entity.Administrator;
import com.techlabs.app.entity.InsurancePlan;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Long> {

	@Query("SELECT p FROM InsurancePlan p JOIN FETCH p.insuranceSchemes")
	List<InsurancePlan> findAllWithSchemes();
}
