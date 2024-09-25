package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CustomerRequestDto {
	
	private String firstName; 
    private String lastName; 
    private LocalDate dob; 
    private long phoneNumber; 
    private Long cityId; // Assuming you will pass city ID 
    private List<Long> insurancePolicyIds; // Assuming you will pass insurance policy IDs 
    private Boolean isActive; // Example of a filter parameter
    private Boolean verified; // Example of a filter parameter

}
