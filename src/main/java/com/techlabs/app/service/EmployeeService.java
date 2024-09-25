package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.DocumentVerificationDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.util.PagedResponse;

import jakarta.validation.Valid;

public interface EmployeeService {

	String registerAgent(@Valid AgentRequestDto agentRequestDto);

	void updateProfile(Long employeeId, EmployeeRequestDto employeeRequestDto);

	void verifyCustomerDocuments(Long customerId);

	void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto);

	void editAgentDetails(Long agentId, AgentRequestDto agentRequestDto);

	String verifyCustomerById(Long customerId);

	void deactivateEmployee(Long id);

	AgentResponseDto findAgentById(long agentId);

	void deactivateAgent(Long id);

	PagedResponse<AgentResponseDto> getAllAgents(int page, int size, String sortBy, String direction);

	//List<Customer> getAllCustomers();

	PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size);

	void verifyCustomer(long customerId);

	void changePassword(Long employeeId, ChangePasswordDto changePasswordDto);

	//boolean verifyDocuments(long customerId, DocumentVerificationDto documentVerificationDto);

	//boolean verifyDocuments(DocumentVerificationDto documentVerificationDto);



}
