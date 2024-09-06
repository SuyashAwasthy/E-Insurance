package com.techlabs.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Nominee {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nominee Name is required")
    @Column(nullable = false)
    private String nomineeName;

    @NotBlank(message = "Relation status is required")
    @Column(nullable = false)
    private String relationStatus;
    
//  @ManyToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
//  @JoinColumn(name = "insurance_policy_id")
//  private InsurancePolicy insurancePolicy; 

}
