package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techlabs.app.dto.InsuranceSchemeDto;

@Entity
@Data
@Table(name = "insurance_plan")
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePlan {

    public InsurancePlan(long insurancePlanId2) {

this.insurancePlanId=insurancePlanId2;
	}



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insurancePlanId;

    @Column(nullable = false)
    private String name;
    
    private boolean active;


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "insurancePlan", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<InsuranceScheme> insuranceSchemes;


	

}