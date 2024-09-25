package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techlabs.app.entity.CustomerQuery;

public interface CustomerQueryRepository extends JpaRepository<CustomerQuery, Long> {
    // Custom query methods if needed
	@Query("SELECT COUNT(q) FROM CustomerQuery q")
	int countAllQueries();



}
