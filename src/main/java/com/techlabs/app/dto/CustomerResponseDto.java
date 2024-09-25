package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class CustomerResponseDto {
    private long customerId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private long phoneNumber;
    private String cityName; // Assuming you want to include city name
    private boolean isActive;
    private boolean verified;
    private LocalDate registrationDate;
    private String stateName;
    
    private String email;
    
	private Long insuranceId;

    // You can include insurance policies if needed
    // private List<InsurancePolicyResponseDto> insurancePolicies;
}
