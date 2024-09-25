package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.PaymentTax;
import com.techlabs.app.entity.Role;

public interface PaymentTaxRepository extends JpaRepository<PaymentTax, Long>{

	Optional<PaymentTax> findFirstByOrderByIdAsc();

}
