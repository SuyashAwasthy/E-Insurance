package com.techlabs.app.repository;

import com.techlabs.app.entity.InsuranceScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceSchemeRepository extends JpaRepository<InsuranceScheme, Long> {
	
}