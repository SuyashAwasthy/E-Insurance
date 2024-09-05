package com.techlabs.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tax_settings")
public class TaxSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taxId;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;
    
    
    @OneToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Column(nullable = false)
    private Double taxRate;

    private String description;

	
	

}


