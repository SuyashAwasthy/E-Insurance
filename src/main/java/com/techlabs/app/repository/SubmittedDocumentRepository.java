package com.techlabs.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SubmittedDocument;
@Repository
public interface SubmittedDocumentRepository extends JpaRepository<SubmittedDocument, Long> {

	

	Optional<SubmittedDocument> findByDocumentName(String documentName);

	 // Query to fetch documents by insurance policy ID
    @Query("SELECT d FROM SubmittedDocument d WHERE d.insurancePolicy.insuranceId = :policyId")
    List<SubmittedDocument> findDocumentsByPolicyId(Long policyId);
	//Optional<SubmittedDocument> findByCustomerIdAndName(long customerId, String documentName);

	//Optional<SubmittedDocument> findByCustomerIdAndName(long customerId, String documentName);
   // Optional<SubmittedDocument> findByCustomerAndDocumentName(Customer customer, String documentName);

}
