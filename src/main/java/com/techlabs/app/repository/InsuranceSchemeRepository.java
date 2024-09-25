package com.techlabs.app.repository;

import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.InsuranceScheme;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceSchemeRepository extends JpaRepository<InsuranceScheme, Long> {

	List<InsuranceScheme> findByInsurancePlan_InsurancePlanId(Long planId);

	//List<InsuranceScheme> findByPlanId(Long planId);
	// List<InsuranceScheme> findByInsurancePlan_Id(Long planId);
//	List<InsuranceScheme> findByInsurancePlan_InsurancePlanId(Long insurancePlanId);
	
	//
//  @Query("SELECT isch FROM InsuranceScheme isch JOIN isch.insurancePolicies ip WHERE ip.insuranceId = :insuranceId")
//    InsuranceScheme findByInsurancePolicyId(@Param("insuranceId") long insuranceId);
  
  Page<InsuranceScheme> findAllByInsurancePlan(InsurancePlan insurancePlan, Pageable pageable);

//List<InsuranceScheme> findByInsurancePlanId(Long id);
////Alternatively, if you need to find InsuranceSchemes by the entire InsurancePlan entity
//List<InsuranceScheme> findByInsurancePlan(InsurancePlan insurancePlan);
  


  // Alternatively, find schemes by the InsurancePlan entity
  List<InsuranceScheme> findByInsurancePlan(InsurancePlan insurancePlan);


}