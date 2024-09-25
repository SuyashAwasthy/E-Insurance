package com.techlabs.app.dto;

import java.util.List;
import java.util.Set;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;

import lombok.Data;

@Data
public class AgentResponseDto {

	public AgentResponseDto(long agentId2, String firstName2, String lastName2, String phoneNumber2) {
		// TODO Auto-generated constructor stub
	}

	public AgentResponseDto() {
		// TODO Auto-generated constructor stub
	}
	
	public AgentResponseDto(long agentId2, String firstName2, String lastName2, String phoneNumber2,String email) {
		
	}


	private Long agentId;

	private UserResponseDto userResponseDto;

	private String name;
	private String firstName;
	private String lastName;
	private String email;

	private String phoneNumber;

	private City city;
	private CityResponseDto cityResponseDto;

//	private Set<Customer> customers;
	private List<Customer> customers;


	private boolean isActive;

//	private Set<Commission> commissions;
	private List<Commission> commissions;


	
	 

}
