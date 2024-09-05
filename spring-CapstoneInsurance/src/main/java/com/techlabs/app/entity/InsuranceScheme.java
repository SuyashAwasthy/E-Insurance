package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "insurance_schemes")
public class InsuranceScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insuranceSchemeId;

    @Column(nullable = false)
    private String insuranceScheme;

    private int minimumPolicyTerm;
    private int maximumPolicyTerm;
    private int minimumAge;
    private int maximumAge;
    private double minimumInvestmentAmount;
    private double maximumInvestmentAmount;
    private double profitRatio;
    private String schemeImage;

    private double newRegistrationCommission;
    private double installmentPaymentCommission;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "insurance_plan_id")
    private InsurancePlan insurancePlan;

    @OneToMany(mappedBy = "insuranceScheme", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<InsurancePolicy> insurancePolicies;

	
   
}
