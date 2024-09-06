package com.techlabs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePolicyDto {
   
	public InsurancePolicyDto(Long insuranceId2, long l, long m, long n, List<Long> nomineeIds2, List<Long> paymentIds2,
			Set<Long> documentIds2, List<Long> customerIds2, LocalDate issuedDate2, LocalDate maturityDate2,
			double premiumAmount2, String policyStatus2, boolean active2) {
		
	}

	private Long insuranceId;
    private Long insuranceSchemeId; // Refers to the associated scheme
    private Long agentId; // Refers to the associated agent
    private Long claimId; // Refers to the associated claim
   
    private List<NomineeDto> nominees;
    private List<Long> nomineeIds; // List of nominee IDs
    private List<Long> paymentIds; // List of payment IDs
    private Set<Long> documentIds; // Set of document IDs
    private List<Long> customerIds; // List of customer IDs
    private LocalDate issuedDate;
    private LocalDate maturityDate;
    private Double premiumAmount;
    private String policyStatus;
    private boolean active;
    private int policyTerm;
    private List<SubmittedDocumentDto> documents;  // Updated to include document details

    private int installmentPeriod;


	

	
}
//private long insuranceId;
//private long insuranceSchemeId;
//private long agentId;
//private long claimId;
//private List<NomineeDto> nominees;
//private List<Long> paymentIds;
//private Set<Long> documentIds;  // If you still want to keep the document IDs
//private List<SubmittedDocumentDto> documents;  // Updated to include document details
//private List<Long> customerIds;
//
//private double premiumAmount;
//private int policyTerm;
//private int installmentPeriod;


