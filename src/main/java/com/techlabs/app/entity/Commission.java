package com.techlabs.app.entity; 
 
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column; 
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.ManyToOne; 
import jakarta.persistence.Table; 
import lombok.Data; 
 
@Entity 
@Data
@Table(name = "commissions") 
public class Commission { 
 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long commissionId; 
 
    @ManyToOne 
    @JoinColumn(name = "agent_id", nullable = false) 
    @JsonIgnore
    private Agent agent; 
 
    @ManyToOne 
    @JoinColumn(name = "policy_id", nullable = false) 
    private InsurancePolicy insurancePolicy; 
 
    @Column(nullable = false) 
    private Double amount; 
 
    @Column(nullable = false) 
    private LocalDateTime date;
    
    @Column(nullable = false)
    private String CommissionType;
    
private double availableCommission;
	
	private double updateCommissionBalance;
    
    

	
	

}