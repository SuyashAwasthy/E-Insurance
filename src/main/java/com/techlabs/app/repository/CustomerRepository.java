package com.techlabs.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsuranceScheme;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	  @Query("SELECT isch FROM InsuranceScheme isch JOIN isch.insurancePolicies ip WHERE ip.insuranceId = :insuranceId")
	    InsuranceScheme findByInsurancePolicyId(@Param("insuranceId") long insuranceId);

	List<Customer> findCustomersByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);

}
