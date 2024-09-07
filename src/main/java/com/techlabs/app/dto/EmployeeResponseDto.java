package com.techlabs.app.dto;

import lombok.Data;

@Data
public class EmployeeResponseDto {

    private Long employeeId;
    private String name;
    private boolean isActive;

    private Long userId;
    private String username;
    private String email;

    //private Long addressId;
   // private String address;
  //  private Long pincode;

  //  private Long stateId;
    //private String stateName;

    private String lastName;
    
    // Getters and Setters

   
   }
