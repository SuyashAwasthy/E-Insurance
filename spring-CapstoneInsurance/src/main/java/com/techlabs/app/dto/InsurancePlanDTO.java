package com.techlabs.app.dto;

import lombok.Data;
import java.util.List;

@Data
public class InsurancePlanDTO {
    private long insurancePlanId;
    private String name;
    private boolean active;
    private List<InsuranceSchemeDto> insuranceSchemes;
	public InsurancePlanDTO(long insurancePlanId, String name, boolean active,
			List<InsuranceSchemeDto> insuranceSchemes) {
		super();
		this.insurancePlanId = insurancePlanId;
		this.name = name;
		this.active = active;
		this.insuranceSchemes = insuranceSchemes;
	}
	public InsurancePlanDTO() {
		}
	public long getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(long insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<InsuranceSchemeDto> getInsuranceSchemes() {
		return insuranceSchemes;
	}
	public void setInsuranceSchemes(List<InsuranceSchemeDto> insuranceSchemes) {
		this.insuranceSchemes = insuranceSchemes;
	}
    
    
}
