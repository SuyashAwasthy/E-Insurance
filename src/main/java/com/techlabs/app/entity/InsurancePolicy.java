package com.techlabs.app.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

@Table(name = "insurance_policy")
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insuranceId;

    @ManyToOne
    @JoinColumn(name = "insurance_scheme_id")
    private InsuranceScheme insuranceScheme;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToOne
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_id")
    private List<Nominee> nominees = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_id")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_id")
    private Set<SubmittedDocument> documents = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "insurancePolicies")
    private List<Customer> customers = new ArrayList<>();

    private LocalDate issuedDate = LocalDate.now();
    private LocalDate maturityDate;
    private double premiumAmount;

    @Column(name = "policy_status")
    private String policyStatus = PolicyStatus.PENDING.name();

    private boolean active;

   

}
