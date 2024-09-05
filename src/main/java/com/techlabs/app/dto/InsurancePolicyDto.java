package com.techlabs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePolicyDto {
   
	private long insuranceId;
    private long insuranceSchemeId; // Refers to the associated scheme
    private long agentId; // Refers to the associated agent
    private long claimId; // Refers to the associated claim
    private List<Long> nomineeIds; // List of nominee IDs
    private List<Long> paymentIds; // List of payment IDs
    private Set<Long> documentIds; // Set of document IDs
    private List<Long> customerIds; // List of customer IDs
    private LocalDate issuedDate;
    private LocalDate maturityDate;
    private double premiumAmount;
    private String policyStatus;
    private boolean active;
	

	
}
