package com.techlabs.app.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techlabs.app.dto.InsurancePolicyDto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "insurance_policy")
public class InsurancePolicy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long insuranceId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "insurance_scheme_id")
  @JsonIgnore
  private InsuranceScheme insuranceScheme;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "agent_id")
  @JsonBackReference  // Back reference in bidirectional relationship
  private Agent agent;

  @OneToOne
  @JoinColumn(name = "claim_id")
  @JsonIgnore
  private Claim claim;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "insurance_policy_id")
  @JsonIgnore
  private List<Nominee> nominees;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "insurance_policy_id")
  @JsonIgnore
  private List<Payment> payments = new ArrayList<>();
  
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "insurance_policy_id")
  @JsonIgnore
  private Set<SubmittedDocument> documents = new HashSet<>();

  @ManyToMany(mappedBy = "insurancePolicies",fetch = FetchType.EAGER)
  @JsonBackReference
  private List<Customer> customers = new ArrayList<>();

  @OneToMany(mappedBy = "insurancePolicy") // Add this line to establish bidirectional relationship
  @JsonIgnore
  private List<Transaction> transactions = new ArrayList<>();

  private LocalDate issuedDate = LocalDate.now();
  private LocalDate maturityDate;
  private double premiumAmount;

  @Column(name = "policy_status")
  private String policyStatus = PolicyStatus.PENDING.name();

  // New Fields
  private int policyTerm; // in years
  private double registeredCommission;
  private int installmentPeriod; // in months
  private double installmentPayment;
  private double totalAmountPaid;
  private double claimAmount;
private boolean active;

@Column(name = "verified")
private Boolean verified=false;



}

//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//private long insuranceId;
//
//@ManyToOne
//@JoinColumn(name = "insurance_scheme_id")
//private InsuranceScheme insuranceScheme;
//
//@ManyToOne(cascade = CascadeType.ALL)
//@JoinColumn(name = "agent_id")
//private Agent agent;
//
//@OneToOne
//@JoinColumn(name = "claim_id")
//private Claim claim;
//
//@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//@JoinColumn(name = "insurance_policy_id")
//private List<Nominee> nominees = new ArrayList<>();
//
//@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//@JoinColumn(name = "insurance_policy_id")
//private List<Payment> payments = new ArrayList<>();
//
//@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//@JoinColumn(name = "insurance_policy_id")
//private Set<SubmittedDocument> documents = new HashSet<>();
//
//@ManyToMany(mappedBy = "insurancePolicies")
//private List<Customer> customers = new ArrayList<>();
//
//@OneToMany(mappedBy = "insurancePolicy")
//private List<Transaction> transactions = new ArrayList<>();
//
//private LocalDate issuedDate = LocalDate.now();
//private LocalDate maturityDate;
//private double premiumAmount;
//
//@Column(name = "policy_status")
//private String policyStatus = PolicyStatus.PENDING.name();
//
//
//private int policyTerm; 
//private double registeredCommission;
//private int installmentPeriod; 
//private double installmentPayment;
//private double totalAmountPaid;
//private double claimAmount;
//
//@Column(name = "verified")
//private boolean verified=false;