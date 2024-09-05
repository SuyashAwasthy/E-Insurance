package com.techlabs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.techlabs.app.entity.InsurancePlan;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePlanDTO {
    public InsurancePlanDTO(InsurancePlan plan) {
		
	}
	private long insurancePlanId;
    private String name;
    private boolean active;
    private List<InsuranceSchemeDto> insuranceSchemes;
	    
}
