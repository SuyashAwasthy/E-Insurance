package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.InsurancePolicy;
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long>{

	Optional<Claim> findByPolicy(InsurancePolicy policy);

}
