package com.techlabs.app.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
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
	
    public InsurancePolicyDto(long insuranceId, long insuranceSchemeId, long agentId, long claimId, 
            List<Long> nomineeIds, List<Long> paymentIds, Set<Long> documentIds, 
            List<Long> customerIds, LocalDate issuedDate, LocalDate maturityDate, 
            double premiumAmount, String policyStatus, boolean active) {
this.insuranceId = insuranceId;
this.insuranceSchemeId = insuranceSchemeId;
this.agentId = agentId;
this.claimId = claimId;
this.nomineeIds = nomineeIds;
this.paymentIds = paymentIds;
this.documentIds = documentIds;
this.customerIds = customerIds;
this.issuedDate = issuedDate;
this.maturityDate = maturityDate;
this.premiumAmount = premiumAmount;
this.policyStatus = policyStatus;
this.active = active;
}

	public InsurancePolicyDto(LocalDate issuedDate2, LocalDate maturityDate2, double premiumAmount2,
			String policyStatus2, boolean active2, long insuranceSchemeId2, long agentId2) {
		this.issuedDate = issuedDate;
        this.maturityDate = maturityDate;
        this.premiumAmount = premiumAmount;
        this.policyStatus = policyStatus;
        this.active = active;
        this.insuranceSchemeId = insuranceSchemeId;
        this.agentId = agentId;	}

	
}
