package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CustomerRequestDto {
	
	private String firstName; 
    private String lastName; 
    private LocalDate dob; 
    private String phoneNumber; 
    private Long cityId; // Assuming you will pass city ID 
    private List<Long> insurancePolicyIds; // Assuming you will pass insurance policy IDs 

}
