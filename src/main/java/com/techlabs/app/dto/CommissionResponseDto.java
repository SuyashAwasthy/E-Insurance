package com.techlabs.app.dto;

import java.time.LocalDateTime;

import com.techlabs.app.entity.Customer;

import lombok.Data;

@Data
public class CommissionResponseDto {
	
	private long commissionId;
    private String commissionType; 
    private LocalDateTime issueDate;
    private double amount;
    private long agentId;   
//    private String agentName;
    private String agentFirstName;
    private String agentLastName;
    private long policyId;
    
    private String firstName;
    private String lastName;
private String name;


}
