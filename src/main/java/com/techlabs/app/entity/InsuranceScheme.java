package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "insurance_schemes")
public class InsuranceScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceSchemeId;

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
    @JsonBackReference
    private InsurancePlan insurancePlan;

    @OneToMany(mappedBy = "insuranceScheme", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InsurancePolicy> insurancePolicies;
    

    @Column(nullable = false)
    private boolean active = true; // Set active to true by default


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "scheme_documents_mapping", joinColumns = @JoinColumn(name = "insurance_scheme_id"), inverseJoinColumns = @JoinColumn(name = "scheme_document_id"))
    private Set<SchemeDocument> schemeDocuments;
	
	
   
}
