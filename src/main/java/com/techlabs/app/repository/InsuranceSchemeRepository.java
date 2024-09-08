package com.techlabs.app.repository;

import com.techlabs.app.entity.InsuranceScheme;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceSchemeRepository extends JpaRepository<InsuranceScheme, Long> {

	List<InsuranceScheme> findByInsurancePlan_InsurancePlanId(Long planId);

	//List<InsuranceScheme> findByPlanId(Long planId);
	// List<InsuranceScheme> findByInsurancePlan_Id(Long planId);
//	List<InsuranceScheme> findByInsurancePlan_InsurancePlanId(Long insurancePlanId);
}