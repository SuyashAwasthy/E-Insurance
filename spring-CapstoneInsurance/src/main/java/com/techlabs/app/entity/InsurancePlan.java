package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "insurance_plan")
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insurancePlanId;

    @Column(nullable = false)
    private String name;
    
    private boolean active;


    
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "insurancePlan", cascade = CascadeType.ALL)
    private List<InsuranceScheme> insuranceSchemes;


	
	
    
}