package com.techlabs.app.dto;

import lombok.Data;

@Data
public class InsuranceSchemeDto {
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
	public long getInsuranceSchemeId() {
		return insuranceSchemeId;
	}
	public void setInsuranceSchemeId(long insuranceSchemeId) {
		this.insuranceSchemeId = insuranceSchemeId;
	}
	public String getInsuranceScheme() {
		return insuranceScheme;
	}
	public void setInsuranceScheme(String insuranceScheme) {
		this.insuranceScheme = insuranceScheme;
	}
	public int getMinimumPolicyTerm() {
		return minimumPolicyTerm;
	}
	public void setMinimumPolicyTerm(int minimumPolicyTerm) {
		this.minimumPolicyTerm = minimumPolicyTerm;
	}
	public int getMaximumPolicyTerm() {
		return maximumPolicyTerm;
	}
	public void setMaximumPolicyTerm(int maximumPolicyTerm) {
		this.maximumPolicyTerm = maximumPolicyTerm;
	}
	public int getMinimumAge() {
		return minimumAge;
	}
	public void setMinimumAge(int minimumAge) {
		this.minimumAge = minimumAge;
	}
	public int getMaximumAge() {
		return maximumAge;
	}
	public void setMaximumAge(int maximumAge) {
		this.maximumAge = maximumAge;
	}
	public double getMinimumInvestmentAmount() {
		return minimumInvestmentAmount;
	}
	public void setMinimumInvestmentAmount(double minimumInvestmentAmount) {
		this.minimumInvestmentAmount = minimumInvestmentAmount;
	}
	public double getMaximumInvestmentAmount() {
		return maximumInvestmentAmount;
	}
	public void setMaximumInvestmentAmount(double maximumInvestmentAmount) {
		this.maximumInvestmentAmount = maximumInvestmentAmount;
	}
	public double getProfitRatio() {
		return profitRatio;
	}
	public void setProfitRatio(double profitRatio) {
		this.profitRatio = profitRatio;
	}
	public String getSchemeImage() {
		return schemeImage;
	}
	public void setSchemeImage(String schemeImage) {
		this.schemeImage = schemeImage;
	}
	public double getNewRegistrationCommission() {
		return newRegistrationCommission;
	}
	public void setNewRegistrationCommission(double newRegistrationCommission) {
		this.newRegistrationCommission = newRegistrationCommission;
	}
	public double getInstallmentPaymentCommission() {
		return installmentPaymentCommission;
	}
	public void setInstallmentPaymentCommission(double installmentPaymentCommission) {
		this.installmentPaymentCommission = installmentPaymentCommission;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(long insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	public InsuranceSchemeDto(String insuranceScheme2, int minimumPolicyTerm2, int maximumPolicyTerm2, int minimumAge2,
			int maximumAge2, double minimumInvestmentAmount2, double maximumInvestmentAmount2, double profitRatio2,
			String schemeImage2, double newRegistrationCommission2, double installmentPaymentCommission2,
			String description2, long insurancePlanId2) {
		this.insuranceSchemeId = insuranceSchemeId;
        this.insuranceScheme = insuranceScheme;
        this.minimumPolicyTerm = minimumPolicyTerm;
        this.maximumPolicyTerm = maximumPolicyTerm;
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
        this.minimumInvestmentAmount = minimumInvestmentAmount;
        this.maximumInvestmentAmount = maximumInvestmentAmount;
        this.profitRatio = profitRatio;
        this.schemeImage = schemeImage;
        this.newRegistrationCommission = newRegistrationCommission;
        this.installmentPaymentCommission = installmentPaymentCommission;
        this.description = description;
        this.insurancePlanId = insurancePlanId;
    }
	public InsuranceSchemeDto(long insuranceSchemeId2, String insuranceScheme2, int minimumPolicyTerm2,
			int maximumPolicyTerm2, int minimumAge2, int maximumAge2, double minimumInvestmentAmount2,
			double maximumInvestmentAmount2, double profitRatio2, String schemeImage2,
			double newRegistrationCommission2, double installmentPaymentCommission2, String description2, long l) {
		 this.insuranceSchemeId = insuranceSchemeId;
	        this.insuranceScheme = insuranceScheme;
	        this.minimumPolicyTerm = minimumPolicyTerm;
	        this.maximumPolicyTerm = maximumPolicyTerm;
	        this.minimumAge = minimumAge;
	        this.maximumAge = maximumAge;
	        this.minimumInvestmentAmount = minimumInvestmentAmount;
	        this.maximumInvestmentAmount = maximumInvestmentAmount;
	        this.profitRatio = profitRatio;
	        this.schemeImage = schemeImage;
	        this.newRegistrationCommission = newRegistrationCommission;
	        this.installmentPaymentCommission = installmentPaymentCommission;
	        this.description = description;
	        this.insurancePlanId = insurancePlanId;	}
    
}
