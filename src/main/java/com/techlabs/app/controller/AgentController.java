package com.techlabs.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.service.AgentService;

@RestController
@RequestMapping("/E-Insurance/agent")
//@PreAuthorize("hasRole('AGENT')")
public class AgentController {
	
	@Autowired
	private AgentService agentService;

	// Register a new agent
	@PostMapping("/register")
	public String registerAgent(@RequestBody AgentRequestDto agentRequestDto) {
		return agentService.registerAgent(agentRequestDto);
	}

	// Get an agent by ID
	 @GetMapping("/agentById/{agentId}")
     public ResponseEntity<AgentResponseDto> viewAgentById(@PathVariable(name = "agentId") long agentId) {
         return new ResponseEntity<AgentResponseDto>(agentService.getAgentById(agentId), HttpStatus.OK);
     }
     

	// Update an agent's profile
	@PutMapping("/{id}")
	public AgentResponseDto updateAgentProfile(@PathVariable Long id, @RequestBody AgentRequestDto agentRequestDto) {
		return agentService.updateAgentProfile(id, agentRequestDto);
	}

	// Change an agent's password
	@PutMapping("/{id}/change-password")
	public void changePassword(@PathVariable Long id, @RequestBody String newPassword) {
		agentService.changePassword(id, newPassword);
	}

//	// Register a new policy under an agent
//	@PostMapping("/{agentId}/policies")
//	public Policy registerPolicy(@PathVariable Long agentId, @RequestBody Policy policy) {
//		return agentService.registerPolicy(agentId, policy);
//	}

	// Calculate commission for a policy
	@GetMapping("/{agentId}/commissions")
	public double calculateCommission(@PathVariable Long agentId, @RequestParam Long policyId) {
		return agentService.calculateCommission(agentId, policyId);
	}

	// Get a list of all agents
//	@GetMapping
//	public List<AgentResponseDto> getAllAgents() {
//		return agentService.getAllAgents();
//	}

	// Withdraw commission for an agent
	@PutMapping("/{agentId}/withdraw")
	public void withdrawCommission(@PathVariable Long agentId, @RequestBody double amount) {
		agentService.withdrawCommission(agentId, amount);
	}
//
//	// Get a list of policies under an agent
//	@GetMapping("/{agentId}/policies")
//	public List<Policy> getAgentPolicies(@PathVariable Long agentId) {
//		return agentService.getAgentPolicies(agentId);
//	}

	// Get an earnings report for an agent
	@GetMapping("/{agentId}/earnings")
	public List<Double> getEarningsReport(@PathVariable Long agentId) {
		return agentService.getEarningsReport(agentId);
	}

	// Get a commission report for an agent
	@GetMapping("/{agentId}/commissions/report")
	public List<Double> getCommissionReport(@PathVariable Long agentId) {
		return agentService.getCommissionReport(agentId);
	}

	@PostMapping("/claim")
	  public ResponseEntity<String> AgentclaimPolicy(@RequestBody ClaimRequestDto claimRequestDto,
	                                            @RequestParam Long agentId) {
	      String response = agentService.agentclaimPolicy(claimRequestDto, agentId);
	      return ResponseEntity.ok(response);
	  }

}
