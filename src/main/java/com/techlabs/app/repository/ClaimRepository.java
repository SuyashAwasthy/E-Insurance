package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.InsurancePolicy;
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long>{

	Optional<Claim> findByPolicy(InsurancePolicy policy);

//	  @Query("SELECT c FROM Claim c WHERE c.policy.insuranceId = :policyId")
//	Optional<Claim> findByPolicyIdAndCustomerId(Long policyId, Long customerId);

	@Query("SELECT c FROM Claim c WHERE c.policy.insuranceId = :policyId")
	Optional<Claim> findByPolicyId(@Param("policyId") Long policyId);

}
