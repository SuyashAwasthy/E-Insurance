package com.techlabs.app.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerId;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private String firstName;
	private String lastName;
	private LocalDate dob;
	private long phoneNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private City city;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "customer_insurance_policy", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "insurance_policy_id"))
	private List<InsurancePolicy> insurancePolicies = new ArrayList<>();

	private boolean isActive = true;
	private boolean verified = false;

	

}
