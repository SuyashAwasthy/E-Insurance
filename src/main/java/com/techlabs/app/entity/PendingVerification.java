package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Data
public class PendingVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "customer_id")
    private long customerId;
    @Column(unique = true)
    private String panCard;
    @Column(unique = true)
    private String aadhaarCard;

    private boolean verified = false;

    // Getters and setters
}