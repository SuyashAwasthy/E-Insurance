package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;

public interface AgentService {

	String registerAgent(AgentRequestDto agentRequestDto);

	AgentResponseDto getAgentById(Long id);

	AgentResponseDto updateAgentProfile(Long id, AgentRequestDto agentRequestDto);

	void changePassword(Long id, String newPassword);

	double calculateCommission(Long agentId, Long policyId);

	void withdrawCommission(Long agentId, double amount);

	List<Double> getEarningsReport(Long agentId);

	List<Double> getCommissionReport(Long agentId);

	String agentclaimPolicy(ClaimRequestDto claimRequestDto, Long agentId);


}
