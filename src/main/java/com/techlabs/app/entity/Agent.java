//package com.techlabs.app.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.util.Set;
//
//@Entity
//@Data
//@Table(name = "agents")
//public class Agent {
//
//	@Id
//	 @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long agentId;
//
//	@OneToOne
//	@JoinColumn(name = "user_id", nullable = false)
//	private User user;
//
//	private String firstName;
//	private String lastName;
//	private String phoneNumber;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "city_id")
//	private City city;
//
////    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
////    private Set<Customer> customers;
//
//	private boolean isActive;
//
//	@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
//	private Set<Commission> commissions;
//
//	private boolean verified = false;
//	
//	 private double totalCommission;
//
////	public void setTotalCommission(double totalCommission) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	public double getTotalCommission() {
////		// TODO Auto-generated method stub
////		return 0;
////	}
//
////	@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
////	private Set<InsurancePolicy> insurancePolicies = new HashSet<>();
//
//}

package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private boolean isActive;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commission> commissions;
    
    
    private double totalCommission;
    
    private boolean verified = false;
    
    @Column(name = "registration_date") 
    private LocalDate registrationDate;

}

