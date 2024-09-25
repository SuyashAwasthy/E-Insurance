package com.techlabs.app.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CustomerRegistrationDto {

    @NotBlank(message = "First name is mandatory")
    private String firstName;
    
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotNull(message = "Date of Birth is mandatory")
    private LocalDate dob;

    @NotNull(message = "Phone number is mandatory")
    private Long phoneNumber;

    @NotNull(message = "City ID is mandatory")
    private Long cityId;

    @NotNull(message = "Active status is mandatory")
    private Boolean isActive;

    @NotNull(message = "Verified status is mandatory")
    private Boolean verified;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

	
    
    
}
