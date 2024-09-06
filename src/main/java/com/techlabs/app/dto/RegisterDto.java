package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class RegisterDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private LocalDate dob;
    private Long phone_number;
    private Set<String> roles;
    private Long cityId; // Add this field
    
    private String panCard;       // Add PAN Card field
    private String aadhaarCard;   // Add Aadhaar Card field

    
	

   
}
