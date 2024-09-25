package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.List;

import com.techlabs.app.entity.City;

import lombok.Data;
@Data
public class CustomerDTO {
	
	 private long customerId;
	    private String firstName;
	    private String lastName;
		    private String email;
		    private City city;
		    private long phoneNumber;
		    private Boolean active;
		    private LocalDate dob;
		    List<InsurancePolicyDto> insurancePolicies;
		    private List<PaymentDto> paymentDto;
			public void setCity(String city_name) {
				// TODO Auto-generated method stub
				
			}
			public CustomerDTO(long customerId2, String firstName2) {
				// TODO Auto-generated constructor stub
			}
			public CustomerDTO() {
				// TODO Auto-generated constructor stub
			}
			public CustomerDTO(long customerId2, boolean active2) {
			this.customerId=customerId2;
				this.active=active2;
			
			}
			
		  
}
