package com.techlabs.app.repository;

import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Role;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

//	List<InsurancePolicy> findByCustomersId(Long customerId);
	

	   @Query("SELECT ip.insuranceScheme FROM InsurancePolicy ip WHERE ip.insuranceId = :insuranceId")
	      InsuranceScheme findInsuranceSchemeByPolicyId(@Param("insuranceId") Long insuranceId);


	   
	   @Query("SELECT ip FROM InsurancePolicy ip JOIN ip.customers c WHERE c.customerId = :customerId")
	   List<InsurancePolicy> findByCustomerId(@Param("customerId") long customerId);

	   @Query("SELECT ip FROM InsurancePolicy ip JOIN ip.customers c WHERE c.customerId = :customerId AND ip.insuranceId = :policyId")
	    InsurancePolicy findByCustomerIdAndPolicyId(@Param("customerId") Long customerId, @Param("policyId") Long policyId);

	//List<InsurancePolicy> findByCustomersCustomerId(Long customerId);

	Page<InsurancePolicy> findByCustomersCustomerId(Long customerId, Pageable pageable);
	 @Query("SELECT ip FROM InsurancePolicy ip LEFT JOIN FETCH ip.payments WHERE ip.insuranceId = :policyId")
	   Optional<InsurancePolicy> findByIdWithPayments(@Param("policyId") long policyId);
	//Optional<Role> findByIdWithPayments(long policyId);

//	 @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM InsurancePolicy p WHERE p.customer.customerId = :customerId AND p.insuranceId = :policyId")
//	    boolean existsByCustomerIdAndInsuranceId(@Param("customerId") Long customerId, @Param("policyId") Long policyId);
	 @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM InsurancePolicy p JOIN p.customers c WHERE c.customerId = :customerId AND p.insuranceId = :policyId")
	    boolean existsByCustomerIdAndInsuranceId(@Param("customerId") Long customerId, @Param("policyId") Long policyId);



//	Optional<InsurancePolicy> findById(Long agentId);
	 List<InsurancePolicy> findByAgent_AgentId(Long agentId);



	 @Query("SELECT COUNT(ip) FROM InsurancePolicy ip") 
     int countAllInsurancePolicies();
	 
	 @Query("SELECT ip FROM InsurancePolicy ip JOIN ip.customers c WHERE c.customerId = :customerId")
	    List<InsurancePolicyDto> findByCustomerId(@Param("customerId") Long customerId);



//	Page<InsurancePolicy> findById(Long customerId, PageRequest pageable);


	 @Query("SELECT p FROM InsurancePolicy p JOIN p.customers c WHERE c.customerId = :customerId")
	    Page<InsurancePolicy> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
	
}