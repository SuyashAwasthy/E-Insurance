package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
@Table(name = "agents")
public class Agent {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long agentId;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private String firstName;
	private String lastName;
	private String phoneNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private City city;

//    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
//    private Set<Customer> customers;

	private boolean isActive;

	@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
	private Set<Commission> commissions;

	private boolean verified = false;

	
}
