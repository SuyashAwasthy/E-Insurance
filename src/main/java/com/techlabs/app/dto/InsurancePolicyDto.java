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
    private String agentName; // Include agent name if necessary
    private Long agentId; // Refers to the associated agent
    private Long claimId; // Refers to the associated claim
    private String insuranceScheme;
    private List<NomineeDto> nominees;
    private List<Long> nomineeIds; // List of nominee IDs
    private List<Long> paymentIds; // List of payment IDs
    public List<Long> getPaymentIds() {
		return paymentIds;
	}
	public void setPaymentIds(List<Long> paymentIds) {
		this.paymentIds = paymentIds;
	}

	

	public InsurancePolicyDto(long insuranceId2, Long insuranceSchemeId2, String name, String insuranceScheme2,
			Long long1, Object object, Long long2, List<Long> collect, List<Long> collect2, Object object2,
			List<CustomerDTO> customerDtos, LocalDate issuedDate2, LocalDate maturityDate2, double premiumAmount2,
			String policyStatus2, boolean active2,String claimedStatus) {
		// TODO Auto-generated constructor stub
	}

	public InsurancePolicyDto(long insuranceId2, Long insuranceSchemeId2, String name, Long long1, String schemeName,
			Long long2, Object object, Long long3, List<Long> collect, List<Long> collect2, Object object2,
			List<CustomerDTO> customerDtos, LocalDate issuedDate2, LocalDate maturityDate2, double premiumAmount2,
			String policyStatus2, boolean active2,String claimedStatus) {
		// TODO Auto-generated constructor stub
	}

	public InsurancePolicyDto(long insuranceId2, Long insuranceSchemeId2, String name, Long long1, String schemeName,
			Long long2, Object object, Long long3, List<Long> collect, List<Long> collect2, Object object2,
			List<CustomerDTO> customerDtos, LocalDate issuedDate2, LocalDate maturityDate2, double premiumAmount2,
			String policyStatus2, boolean active2) {
		// TODO Auto-generated constructor stub
	}

	private Set<Long> documentIds; // Set of document IDs
    private List<Long> customerIds; // List of customer IDs
    private List<CustomerDTO> customers; // Add customer DTO
    private LocalDate issuedDate;
    private LocalDate maturityDate;
    private Double premiumAmount;
    private String policyStatus;
    private boolean active;
    private int policyTerm;
    private Set<SubmittedDocumentDto> documents;  // Updated to include document details

    private int installmentPeriod;
    
    private double amount;
    private String paymentMethodId;
    private long policyId;
    private String paymentType;
    private double tax;
    private double totalPayment;
private String insurancePlan;
private Double profitRatio;
private List<PaymentDto> payments;
private Double claimAmount;
private String claimedStatus;

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


