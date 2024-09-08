package com.techlabs.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Documentt;
import com.techlabs.app.entity.SubmittedDocument;

@Repository
public interface DocumentRepository extends JpaRepository<SubmittedDocument, Long>{

	void save(Documentt document);

//	List<Documentt> findByCustomer_CustomerId(Long customerId);

	

}
