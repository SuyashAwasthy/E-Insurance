package com.techlabs.app.service;

import java.io.IOException;
import java.util.List;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CommissionResponseDto;
import com.techlabs.app.dto.CustomerRegistrationDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.util.PagedResponse;

public interface AgentService {

	String registerAgent(AgentRequestDto agentRequestDto);

	AgentResponseDto getAgentById(Long id);

	AgentResponseDto updateAgentProfile(Long id, AgentRequestDto agentRequestDto);

	//void changePassword(Long id, String newPassword);

	double calculateCommission(Long agentId, Long policyId);

//	void withdrawCommission(Long agentId, double amount);

	List<Double> getEarningsReport(Long agentId);

	List<CommissionResponseDto> getCommissionReport(Long agentId);

	String agentclaimPolicy(ClaimRequestDto claimRequestDto, Long agentId);

	PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size);

	Agent getAgentByUsername(String username);

	List<Customer> getCustomersByCity(Long id);

	Agent getAgentByUserId(Long id);

	String registerCustomer(RegisterDto customerRegistrationDto) throws IOException;

	void withdrawCommission(Long agentId, double amount, Long insurancePolicyId);

	void changePassword(Long agentId, ChangePasswordDto changePasswordDto);

	List<AgentResponseDto> getAgentsByActiveStatus(boolean active);

	int countActiveAgents();

	double getTotalCommission(Long agentId);

	//AgentResponseDto getAgentByName(String name);

	//AgentResponseDto getAgentByName(String firstName, String lastName);

	//String registerCustomer(CustomerRegistrationDto customerRegistrationDto);

	//String registerCustomer(RegisterDto registerDto) throws IOException;


}
