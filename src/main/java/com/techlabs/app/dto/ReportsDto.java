package com.techlabs.app.dto;

import lombok.Data;

@Data
public class ReportsDto {
	private Long customerCount;
    private Long agentCount;
    private Long employeeCount;
    private Long policyCount;
    private Double customerPolicyRatio;

}
