package com.techlabs.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @ManyToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Column(nullable = false)
    private String transactionType;  // e.g., PREMIUM_PAYMENT, CLAIM, WITHDRAWAL

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date;

    private String status;  // e.g., SUCCESS, PENDING, FAILED

	
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;
    
    private String type;

    
}
