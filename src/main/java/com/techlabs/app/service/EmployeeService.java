package com.techlabs.app.service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.EmployeeRequestDto;

import jakarta.validation.Valid;

public interface EmployeeService {

	String registerAgent(@Valid AgentRequestDto agentRequestDto);

	void updateProfile(Long employeeId, EmployeeRequestDto employeeRequestDto);

	void verifyCustomerDocuments(Long customerId);

	void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto);

	void editAgentDetails(Long agentId, AgentRequestDto agentRequestDto);

}
