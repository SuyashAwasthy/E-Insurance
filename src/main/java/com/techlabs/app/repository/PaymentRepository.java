package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.SubmittedDocument;
@Repository
public interface PaymentRepository  extends JpaRepository<Payment, Long>{

}
