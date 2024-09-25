package com.techlabs.app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	  @Query("SELECT isch FROM InsuranceScheme isch JOIN isch.insurancePolicies ip WHERE ip.insuranceId = :insuranceId")
	    InsuranceScheme findByInsurancePolicyId(@Param("insuranceId") long insuranceId);

	List<Customer> findCustomersByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);

	

//	Optional<User> findByUserId(Long id);
	Optional<Customer> findByUserId(Long userId);

	//List<Customer> findByCityCityId(Long id);

    List<Customer> findByCityId(Long cityId);

	Optional<Customer> findByUser(User existingUser);

	Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name, String name2,
			Pageable pageable);

	Page<Customer> findByIsActive(boolean active, Pageable pageable);

	Optional<Role> findByCustomerId(Long id);

	 @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.email = :email")
	    Optional<Customer> findByUserEmail(@Param("email") String email);

	//Optional<User> findByUserDetails(User user);

}
