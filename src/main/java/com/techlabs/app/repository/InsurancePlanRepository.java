package com.techlabs.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.entity.Administrator;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.InsuranceScheme;
@Repository
public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Long> {
//
//	@Query("SELECT p FROM InsurancePlan p JOIN FETCH p.insuranceSchemes")
//	List<InsurancePlan> findAllWithSchemes();

	Optional<InsurancePlan> findByName(String name);

	//Page<InsurancePlan> findAllWithSchemes(PageRequest pageable);
//	Page<InsuranceScheme> findAllByInsurancePlan(InsurancePlan insurancePlan, Pageable pageable);
	
	
	Page<InsurancePlan> findAll(Pageable pageable);

	List<InsurancePlan> findByNameContainingIgnoreCase(String name);
	
	  
}
