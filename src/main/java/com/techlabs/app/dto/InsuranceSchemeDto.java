package com.techlabs.app.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceSchemeDto {
    public InsuranceSchemeDto(long insuranceSchemeId2, String insuranceScheme2, int minimumPolicyTerm2,
			int maximumPolicyTerm2, int minimumAge2, int maximumAge2, double minimumInvestmentAmount2,
			double maximumInvestmentAmount2, double profitRatio2, String schemeImage2,
			double newRegistrationCommission2, double installmentPaymentCommission2, String description2) {
		// TODO Auto-generated constructor stub
	}
	public InsuranceSchemeDto(Long insuranceSchemeId2, String insuranceScheme2, int minimumPolicyTerm2,
			int maximumPolicyTerm2, int minimumAge2, int maximumAge2, double minimumInvestmentAmount2,
			double maximumInvestmentAmount2, double profitRatio2, String schemeImage2,
			double newRegistrationCommission2, double installmentPaymentCommission2, String description2, long l) {
		// TODO Auto-generated constructor stub
	}
	private long insuranceSchemeId;
    private String insuranceScheme;
    private int minimumPolicyTerm;
    private int maximumPolicyTerm;
    private int minimumAge;
    private int maximumAge;
    private double minimumInvestmentAmount;
    private double maximumInvestmentAmount;
    private double profitRatio;
    private String schemeImage;
    private double newRegistrationCommission;
    private double installmentPaymentCommission;
    private String description;
    private long insurancePlanId;
    
    private Set<SchemeDocumentDto> schemeDocument=new HashSet<>();

	private boolean isActive;
	
	   
	

}
