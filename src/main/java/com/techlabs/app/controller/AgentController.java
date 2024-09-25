package com.techlabs.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CommissionResponseDto;
import com.techlabs.app.dto.ContactFormRequestDto;
import com.techlabs.app.dto.ContactReplyRequestDto;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.CustomerRegistrationDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.ContactMessage;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.User;
import com.techlabs.app.entity.WithdrawalRequest;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.service.AgentService;
import com.techlabs.app.service.ContactFormService;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.service.InsurancePolicyService;
import com.techlabs.app.service.WithdrawalRequestService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/E-Insurance/agent")
//@PreAuthorize("hasRole('AGENT')")
public class AgentController {

	private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

	@Autowired
	private AgentService agentService;

	@Autowired
	private ContactFormService contactFormService;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private InsurancePolicyService insurancePolicyService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private WithdrawalRequestService withdrawalRequestService; 
	
	

	@Operation(summary = "Add Customer")
	@PostMapping("addCustomer")
	public ResponseEntity<Integer> registerCustomer(@RequestBody RegisterDto customerRegistrationDto)
			throws IOException {
		logger.info("Request to register customer: {}", customerRegistrationDto);
		Integer customerId = Integer.parseInt(agentService.registerCustomer(customerRegistrationDto));
		return new ResponseEntity<Integer>(customerId, HttpStatus.ACCEPTED);
	}

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
	@PutMapping("/{customerId}/change-password") 
    public ResponseEntity<String> changePassword(@PathVariable Long agentId, @RequestBody ChangePasswordDto changePasswordDto) { 
        customerService.changePassword(agentId, changePasswordDto); 
        return ResponseEntity.ok("Password changed successfully"); 
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
	@PostMapping("/withdraw")
    public ResponseEntity<String> withdrawCommission(@RequestParam Long agentId, 
                                                      @RequestParam double amount,
                                                      @RequestParam Long insurancePolicyId) {
        agentService.withdrawCommission(agentId, amount, insurancePolicyId);
        return ResponseEntity.ok("Withdrawal successful");
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
	public List<CommissionResponseDto> getCommissionReport(@PathVariable Long agentId) {
		return agentService.getCommissionReport(agentId);
	}

	@PostMapping("/claim")
	public ResponseEntity<String> AgentclaimPolicy(@RequestBody ClaimRequestDto claimRequestDto,
			@RequestParam Long agentId) {
		String response = agentService.agentclaimPolicy(claimRequestDto, agentId);
		return ResponseEntity.ok(response);
	}

//	@GetMapping("/get-all-customers")
//	public ResponseEntity<List<Customer>> getAllCustomers() {
//		List<Customer> customers = customerService.getAllCustomers();
//		return new ResponseEntity<>(customers, HttpStatus.OK);
//	}
//	
//	

	@Operation(summary = "TO  get All Customers")
	@GetMapping("/get-all-customers")
	public ResponseEntity<PagedResponse<CustomerResponseDto>> getAllCustomers(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size) {

		logger.info("Received request to get all customers with page {} and size {}.", page, size);
		PagedResponse<CustomerResponseDto> pagedResponse = agentService.getAllCustomers(page, size);
		logger.info("Retrieved {} customers on page {}.", pagedResponse.getContent().size(), page);
		return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
	}

	@GetMapping("/contact/messages/{agentId}")
	public ResponseEntity<List<ContactMessage>> getContactMessages(@PathVariable Long agentId) {
		List<ContactMessage> messages = contactFormService.getAllContactMessagesForAgent(agentId);
		return new ResponseEntity<>(messages, HttpStatus.OK);
	}

	@PostMapping("/contact/reply")
	public ResponseEntity<String> replyToContactMessage(@RequestBody ContactReplyRequestDto replyRequest) {
		contactFormService.submitReply(replyRequest);
		return new ResponseEntity<>("Reply submitted successfully", HttpStatus.OK);
	}

	@GetMapping("/queries")
	public ResponseEntity<List<ContactFormRequestDto>> viewAllQueries() {
		List<ContactFormRequestDto> queries = contactFormService.getAllQueries();
		return new ResponseEntity<>(queries, HttpStatus.OK);
	}

	@Operation(summary = "TO  get Agent username")
	@GetMapping("/profile")
	public Agent getProfile(Authentication authentication) {
		// Get the username from the authentication object
		String username = authentication.getName();
		// Fetch the agent details based on the username
		return agentService.getAgentByUsername(username);
	}

	@Operation(summary = "TO  get customer by city")
	@GetMapping("/customers")
	public List<Customer> getCustomersByAgentCity(Authentication authentication) {

		String username = authentication.getName();
		System.out.println("Authenticated Username: " + username);

		// Fetch the agent by username
		Agent agent = agentService.getAgentByUsername(username);

		if (agent == null) {
			throw new RuntimeException("Agent not found");
		}

		if (agent.getCity() == null) {
			throw new RuntimeException("City not found for agent: " + username);
		}
		System.out.println("Agent City ID: " + agent.getCity().getId());

		return agentService.getCustomersByCity(agent.getCity().getId());
	}

	@Operation(summary = "TO  get customersssssssssssss by city")
	@GetMapping("/customers/city/{cityId}")
	public List<Customer> getCustomersByCity(@PathVariable Long cityId) {
		return customerService.findByCityCityId(cityId);
	}
	
//	// GET /api/agents/{agentId}/policies
//    @GetMapping("/{agentId}/policies")
//    public ResponseEntity<Optional<InsurancePolicyDto>> getPoliciesByAgentId(@PathVariable Long agentId) {
//        Optional<InsurancePolicyDto> policies = insurancePolicyService.getPoliciesByAgentId(agentId);
//
//        if (policies.isEmpty()) {
//            return ResponseEntity.notFound().build();  // Return 404 if no policies are found
//        }
//        return ResponseEntity.ok(policies);  // Return 200 OK with the list of policies
//    }
	 @GetMapping("/{agentId}/policies")
	    public ResponseEntity<List<InsurancePolicy>> getPoliciesByAgentId(@PathVariable Long agentId) {
	        List<InsurancePolicy> policies = insurancePolicyService.getPoliciesByAgentId(agentId);
	        return ResponseEntity.ok(policies);
	    }
	 
	 @GetMapping("/customer/{email}")
	 public ResponseEntity<CustomerDTO> getCustomerStatus(@PathVariable String email) {
		 System.out.println("email issss"+email);
	     CustomerDTO status = customerService.checkCustomerStatus(email);
	     return ResponseEntity.ok(status);
	 }
	 @PostMapping("/withdrawals")
	 @Operation(summary = "Create Agent Withdrawal Request")
	 public ResponseEntity<String> createAgentWithdrawalRequest(@RequestParam double amount) {
	  withdrawalRequestService.createWithdrawalRequest(amount);
	  return ResponseEntity.ok("Withdrawal request created successfully");
	 }
	 
	 @GetMapping("/{agentId}/totalCommission")
	 public ResponseEntity<Double> getTotalCommission(@PathVariable Long agentId) {
	  double totalCommission = agentService.getTotalCommission(agentId);
	  return ResponseEntity.ok(totalCommission);
	 }
//	 @GetMapping("/all-customer-by-agent")
//	 public ResponseEntity<List<CustomerResponseDto>> getAllCustomerByAgent() {
//	     List<CustomerResponseDto> customers = customerService.getAllCustomerByAgent();
//	     return new ResponseEntity<List<CustomerResponseDto>>(customers, HttpStatus.OK);
//	 }
//	 
	 
	 @GetMapping("/all-customer-by-agent")
	 public ResponseEntity<PagedResponse<CustomerResponseDto>> getAllCustomerByAgent(
	     @RequestParam(name = "page", defaultValue = "0") int page,
	     @RequestParam(name = "size", defaultValue = "5") int size) {
	     PagedResponse<CustomerResponseDto> customers = customerService.getAllCustomerByAgent(page, size);
	     return new ResponseEntity<>(customers, HttpStatus.OK);
	 }

	 @GetMapping("/customers/{email}")
	    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
		 Customer customer = customerService.findCustomerByEmail(email);
	        
	        // If customer is null, return 404 (Not Found) status
	        if (customer == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        // Manually map the Customer entity to the CustomerDto
	        CustomerDTO customerDto = new CustomerDTO();
	        customerDto.setCustomerId(customer.getCustomerId());
	        customerDto.setFirstName(customer.getFirstName());
	        customerDto.setLastName(customer.getLastName());
	        customerDto.setEmail(customer.getUser().getEmail());
	        customerDto.setPhoneNumber(customer.getPhoneNumber());
	        customerDto.setDob(customer.getDob());
	        customerDto.setCity(customer.getCity().getCity_name());

	        // Return the CustomerDto with 200 OK status
	        return ResponseEntity.ok(customerDto);
	    }
	 
	 
	 @GetMapping("/{agentId}/withdrawal-requests")
	    public ResponseEntity<List<WithdrawalRequest>> getWithdrawalRequestsByAgent(@PathVariable long agentId) {
	        List<WithdrawalRequest> requests = withdrawalRequestService.getWithdrawalRequestsByAgentId(agentId);
	        return ResponseEntity.ok(requests);
	    }
	 
	 
	 
	 
}
