package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.PendingVerification;

@Repository
public interface PendingVerificationRepository extends JpaRepository<PendingVerification, Long> {

	Optional<PendingVerification> findByCustomerId(Long customerId);

}
