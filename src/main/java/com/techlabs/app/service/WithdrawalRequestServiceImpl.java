package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.techlabs.app.dto.WithdrawalRequestDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.User;
import com.techlabs.app.entity.WithdrawalRequest;
import com.techlabs.app.entity.WithdrawalRequestStatus;
import com.techlabs.app.entity.WithdrawalRequestType;
import com.techlabs.app.exception.AgentNotFoundException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.repository.WithdrawalRequestRepository;

import org.springframework.stereotype.Service;

@Service
public class WithdrawalRequestServiceImpl implements WithdrawalRequestService{
	@Autowired
	private WithdrawalRequestRepository withdrawalRequestRepository;
	
	@Autowired
	private AgentRepository agentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	

	@Override
	public WithdrawalRequest updateWithdrawalRequest(long withdrawalRequestId, WithdrawalRequest withdrawalRequest) {
		if (withdrawalRequestRepository.existsById(withdrawalRequestId)) {
			withdrawalRequest.setWithdrawalRequestId(withdrawalRequestId);
			return withdrawalRequestRepository.save(withdrawalRequest);
		}
		throw new IllegalArgumentException("WithdrawalRequest not found");
	}

	@Override
	public void deleteWithdrawalRequest(long withdrawalRequestId) {
		withdrawalRequestRepository.deleteById(withdrawalRequestId);
	}

	@Override
	public WithdrawalRequest getWithdrawalRequestById(long withdrawalRequestId) {
		Optional<WithdrawalRequest> optionalWithdrawalRequest = withdrawalRequestRepository
				.findById(withdrawalRequestId);
		if (optionalWithdrawalRequest.isPresent()) {
			return optionalWithdrawalRequest.get();
		}
		throw new IllegalArgumentException("WithdrawalRequest not found");
	}

	
    // Conversion method
    private WithdrawalRequestDto convertToDto(WithdrawalRequest withdrawalRequest) {
        WithdrawalRequestDto dto = new WithdrawalRequestDto();
        dto.setWithdrawalRequestId(withdrawalRequest.getWithdrawalRequestId());
        dto.setAgentId(withdrawalRequest.getAgent() != null ? withdrawalRequest.getAgent().getAgentId() : null);
        dto.setRequestType(withdrawalRequest.getRequestType());
        dto.setRequestDate(withdrawalRequest.getRequestDate());
        dto.setStatus(withdrawalRequest.getStatus());
        dto.setApprovedAt(withdrawalRequest.getApprovedAt());
        dto.setAmount(withdrawalRequest.getAmount());
        return dto;
    }
    @Override
public List<WithdrawalRequestDto> getAllWithdrawalRequests() {
        List<WithdrawalRequest> withdrawalRequests = withdrawalRequestRepository.findAll();
        return withdrawalRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
	@Override
	public List<WithdrawalRequest> getWithdrawalRequestsByStatus(String status) {
		return withdrawalRequestRepository.findByStatus(status);
	}

//	@Override
//	public void createWithdrawalRequest(double amount) {
//		Agent agent = agentRepository.findById(getAgentDetails().getAgentId())
//			    .orElseThrow(() -> new AgentNotFoundException(
//			      "Agent not found with ID: " + getAgentDetails().getAgentId()));
//			  if (amount > agent.getTotalCommission()) {
//				  //Https add karna hai, then change exception
//			   throw new AllExceptions.UserNotFoundException("No sufficient commission balance");
//			  }
//			  WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
//			  agent.setTotalCommission(agent.getTotalCommission()-amount);
//			  agentRepository.save(agent);
//			  withdrawalRequest.setAgent(agent);
//
//			  withdrawalRequest.setAmount(amount);
//			  withdrawalRequest.setRequestDate(LocalDateTime.now());
//			  withdrawalRequest.setStatus(WithdrawalRequestStatus.PENDING);
//			  withdrawalRequest.setRequestType(WithdrawalRequestType.COMMISSION_WITHDRAWAL);
//
//			  withdrawalRequestRepository.save(withdrawalRequest);
//		
//	}
	
//	private Agent getAgentDetails() {
//		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		  if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//		   UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//		   return agentRepository.findByUser(userRepository
//		     .findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
//		     .orElseThrow(() -> new AllExceptions.UserNotFoundException("User not found")));
//		  }
//		  throw new AllExceptions.WithdrawNotFoundException("agent not found");
//	}
	
	// In your service class
	@Override
	public void createWithdrawalRequest(double amount) {
	    Agent agent = getAgentDetails();
	    agent.getTotalCommission();
	    
	    if (amount > agent.getTotalCommission()) {
	        throw new AllExceptions.UserNotFoundException("Insufficient commission balance");
	    }
	    
	    WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
	    withdrawalRequest.setAgent(agent);
	    withdrawalRequest.setUser(agent.getUser());  // Set the user from the agent
	    
	    withdrawalRequest.setAmount(amount);
	    withdrawalRequest.setRequestDate(LocalDateTime.now());
	    withdrawalRequest.setStatus(WithdrawalRequestStatus.PENDING);
	    withdrawalRequest.setRequestType(WithdrawalRequestType.COMMISSION_WITHDRAWAL);
	    
	    agent.setTotalCommission(agent.getTotalCommission() - amount);
	    agentRepository.save(agent);
	    
	    withdrawalRequestRepository.save(withdrawalRequest);
	}


	private Agent getAgentDetails() {
	    // Fetch the current authenticated user
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // Check if authentication and principal are valid
	    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        
	        // Find user by username or email
	        User user = userRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
	                .orElseThrow(() -> new AllExceptions.UserNotFoundException("User not found"));

	        // Use your existing repository method to find the agent by the User object
	        return agentRepository.findByUser(user)
	                .orElseThrow(() -> new AllExceptions.UserNotFoundException("Agent not found for the given user"));
	    }
	    
	    // Throw an exception if authentication is invalid
	    throw new AllExceptions.WithdrawNotFoundException("Agent not found");
	}


	@Override
	 public void approveWithdrawal(long withdrawalId) {
	  WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.findById(withdrawalId)
	    .orElseThrow(() -> new AllExceptions.WithdrawNotFoundException("Withdrawal request not found with ID: " + withdrawalId));

	  if (!withdrawalRequest.getStatus().equals(WithdrawalRequestStatus.PENDING)) {
		  //Https add karna hai, then change exception
	   throw new AllExceptions.WithdrawNotFoundException("Only pending withdrawal requests can be approved.");
	  }
	  Agent agent = withdrawalRequest.getAgent();
	  withdrawalRequest.setStatus(WithdrawalRequestStatus.APPROVED);
	  withdrawalRequest.setApprovedAt(LocalDateTime.now());
	  withdrawalRequestRepository.save(withdrawalRequest);
	  //emailService.sendWithdrawalApprovalMail(withdrawalRequest);
	 }

	@Override
	public void rejectWithdrawal(long withdrawalId) {
		WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.findById(withdrawalId)
				.orElseThrow(() -> new AllExceptions.WithdrawNotFoundException(
						"Withdrawal request not found with ID: " + withdrawalId));

		if (!withdrawalRequest.getStatus().equals(WithdrawalRequestStatus.PENDING)) {
			throw new AllExceptions.WithdrawNotFoundException("Only pending withdrawal requests can be rejected.");
		}

		withdrawalRequest.setStatus(WithdrawalRequestStatus.REJECTED);
		withdrawalRequestRepository.save(withdrawalRequest);
		
	}

	@Override
	 public List<WithdrawalRequest> getWithdrawalRequestsByAgentId(long agentId) {
	        return withdrawalRequestRepository.findByAgent_AgentId(agentId);
	    }
}
