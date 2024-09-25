package com.techlabs.app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CommissionResponseDto;
import com.techlabs.app.dto.CustomerRegistrationDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.UserResponseDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AgentNotFoundException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.TransactionRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.repository.WithdrawalRequestRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class AgentServiceImpl implements AgentService{

	@Autowired
    private AgentRepository agentRepository;
//
//    @Autowired
//    private PolicyRepository policyRepository;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;
	
	@Autowired
	private ClaimRepository claimRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CommissionRepository commissionRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private WithdrawalRequestRepository withdrawalRequestRepository;

	public AgentServiceImpl(AgentRepository agentRepository, PasswordEncoder passwordEncoder,
			UserRepository userRepository, CityRepository cityRepository, RoleRepository roleRepository,
			InsurancePolicyRepository insurancePolicyRepository, ClaimRepository claimRepository,
			CustomerRepository customerRepository, CommissionRepository commissionRepository,
			TransactionRepository transactionRepository) {
		super();
		this.agentRepository = agentRepository;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.cityRepository = cityRepository;
		this.roleRepository = roleRepository;
		this.insurancePolicyRepository = insurancePolicyRepository;
		this.claimRepository = claimRepository;
		this.customerRepository = customerRepository;
		this.commissionRepository = commissionRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	@Transactional
	public String registerAgent(AgentRequestDto agentRequestDto) {
		if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}
		if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		
		User user = new User();
		user.setUsername(agentRequestDto.getUsername());
		user.setEmail(agentRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));

		
		Role agentRole = roleRepository.findByName("ROLE_AGENT")
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_AGENT"));
		Set<Role> roles = new HashSet<>();
		roles.add(agentRole);
		user.setRoles(roles);

		
		User savedUser = userRepository.save(user);

		
		City city = cityRepository.findById(agentRequestDto.getCity_id())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
						"City not found with id: " + agentRequestDto.getCity_id()));

		
		Agent agent = new Agent();
		agent.setAgentId(savedUser.getId());
		agent.setUser(savedUser);
		agent.setFirstName(agentRequestDto.getFirstName());
		agent.setLastName(agentRequestDto.getLastName());
		agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
		agent.setCity(city); 
		//agent.setActive(agentRequestDto.isActive());
		agent.setActive(true);
		agent.setVerified(false);
		agentRepository.save(agent);
		return "Agent Registered successfully";
	}

	@Override
	public AgentResponseDto getAgentById(Long id) {
		Agent agent = agentRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));

	    return convertAgentToAgentResponseDto(agent);  
	}

	private AgentResponseDto convertAgentToAgentResponseDto(Agent agent) {
		if (agent == null) {
	        throw new IllegalArgumentException("Agent must not be null");
	    }
	    AgentResponseDto agentDto = new AgentResponseDto();

	 
	    agentDto.setAgentId(agent.getAgentId());
	    agentDto.setName(agent.getFirstName() + " " + agent.getLastName()); // Combined name
	    agentDto.setPhoneNumber(agent.getPhoneNumber());
	    agentDto.setActive(agent.isActive());

	    // Convert and set the associated City entity to CityResponseDto
//	    if (agent.getCity() != null) {
//	        CityResponseDto cityDto = convertCityToCityResponseDto(agent.getCity());
//	        agentDto.setCity(cityDto);
//	    }
	//
//	    // Convert and set the associated User entity to UserResponseDto
	    if (agent.getUser() != null) {
	        UserResponseDto userDto = new UserResponseDto();
	        userDto.setId(agent.getUser().getId());
	        userDto.setUsername(agent.getUser().getUsername());
	        userDto.setEmail(agent.getUser().getEmail());
	        //agentDto.setUserResponseDto(userDto);
	    }

	    // Convert and set associated lists (e.g., customers and commissions)
//	    if (agent.getCustomers() != null) {
//	        List<Customer> customers = agent.getCustomers().stream().collect(Collectors.toList());
//	        agentDto.setCustomers(customers);
//	    }

	    if (agent.getCommissions() != null) {
	        List<Commission> commissions = agent.getCommissions().stream().collect(Collectors.toList());
	        agentDto.setCommissions(commissions);
	    }

	    return agentDto;
	}

	@Override
	public AgentResponseDto updateAgentProfile(Long id, AgentRequestDto agentRequestDto) {
		Agent agent = agentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + id));

		agent.setFirstName(agentRequestDto.getFirstName());
		agent.setLastName(agentRequestDto.getLastName());
		agent.setPhoneNumber(agentRequestDto.getPhoneNumber());

		Agent updatedAgent = agentRepository.save(agent);

		return new AgentResponseDto(updatedAgent.getAgentId(), updatedAgent.getFirstName(), updatedAgent.getLastName(),
				updatedAgent.getPhoneNumber());
	}


	
	@Override
	public void changePassword(Long agentId, ChangePasswordDto changePasswordDto) {
		 Customer customer = customerRepository.findById(agentId) 
		            .orElseThrow(() -> new RuntimeException("Customer not found")); 
		        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), customer.getUser().getPassword())) { 
		            throw new RuntimeException("Incorrect old password"); 
		        } 
		        customer.getUser().setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword())); 
		       customerRepository.save(customer); 
		    
		
	}

	@Override
	public double calculateCommission(Long agentId, Long policyId) {
		// Retrieve the agent by ID
				Agent agent = agentRepository.findById(agentId)
						.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

				// Retrieve the insurance policy by ID
				InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
						.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy not found with ID: " + policyId));

				InsuranceScheme insuranceScheme = insurancePolicy.getInsuranceScheme();
				// Retrieve the commission rate and policy premium
				double commissionRate = insuranceScheme.getProfitRatio(); // Commission rate should be > 0
				double policyPremium = insurancePolicy.getPremiumAmount(); // Policy premium should be > 0

				// Check if the values are correct (for debugging)
				System.out.println("Commission Rate: " + commissionRate);
				System.out.println("Policy Premium: " + policyPremium);

				// If commission rate or policy premium are 0, log an error
				if (commissionRate <= 0) {
					throw new IllegalArgumentException("Invalid commission rate: " + commissionRate);
				}
				if (policyPremium <= 0) {
					throw new IllegalArgumentException("Invalid policy premium: " + policyPremium);
				}

				// Calculate the commission
				double commission = policyPremium * (commissionRate / 100);

				// Log the calculated commission (for debugging)
				System.out.println("Calculated Commission: " + commission);

				// Save commission data in the Commission table
				Commission commissionEntry = new Commission();
				commissionEntry.setAgent(agent);
				commissionEntry.setInsurancePolicy(insurancePolicy);
				commissionEntry.setAmount(commission);

				// Set commission type and date
				commissionEntry.setCommissionType("Standard"); // Modify commission type based on business logic
				commissionEntry.setDate(LocalDateTime.now());

				// Save the commission in the repository
				commissionRepository.save(commissionEntry);

				return commission;	
	
	}

	@Override
	@Transactional
	public void withdrawCommission(Long agentId, double amount, Long insurancePolicyId) {
		// Fetch the agent
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

		// Fetch the insurance policy
		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(insurancePolicyId).orElseThrow(
				() -> new ResourceNotFoundException("InsurancePolicy not found with ID: " + insurancePolicyId));

		// Fetch total commission amount for the agent
		Double totalCommission = commissionRepository.getTotalCommissionForAgent(agentId);

		// Check if total commission is null or less than the requested withdrawal
		// amount
		if (totalCommission == null || amount > totalCommission) {
			throw new IllegalArgumentException("Insufficient commission balance to withdraw");
		}

		// Create a transaction for commission withdrawal
		Transaction transaction = new Transaction();
		transaction.setAgent(agent);
		transaction.setAmount(amount);
		transaction.setTransactionType("WITHDRAWAL");
		
		transaction.setDate(LocalDateTime.now());
		transaction.setInsurancePolicy(insurancePolicy);

		transactionRepository.save(transaction);

		// Update agent's commission balance
		updateCommissionBalance(agentId, amount);
	}
	private void updateCommissionBalance(Long agentId, double amount) {
		// Fetch all commissions for the agent
		List<Commission> commissions = commissionRepository.findCommissionsForAgent(agentId);

		// Deduct the withdrawal amount from commissions
		double remainingAmount = amount;

		for (Commission commission : commissions) {
			if (remainingAmount <= 0)
				break;

			double currentAmount = commission.getAmount();
			if (currentAmount > remainingAmount) {
				commission.setAmount(currentAmount - remainingAmount);
				remainingAmount = 0;
			} else {
				remainingAmount -= currentAmount;
				commission.setAmount(0.0);
			}

			commissionRepository.save(commission);
		}

		// Ensure that remainingAmount is zero or less
		if (remainingAmount > 0) {
			throw new IllegalStateException("Error: Not enough commission balance to complete the withdrawal.");
		}
	}


	@Override
	public List<Double> getEarningsReport(Long agentId) {
		// Fetch the Agent entity by its ID
				Agent agent = agentRepository.findById(agentId)
						.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

				// Fetch transactions for the given agent with the type "DEPOSIT"
				List<Transaction> earnings = transactionRepository.findAllByAgentAndType(agent, "DEPOSIT");

				// Map the list of Transaction objects to a list of amounts
				return earnings.stream().map(Transaction::getAmount) // Extract amount from each transaction
						.collect(Collectors.toList()); // Collect into a List<Double>
	}

	@Override
	 public List<CommissionResponseDto> getCommissionReport(Long agentId) {
	     // Fetch the Agent entity by its ID
	     Agent agent = agentRepository.findById(agentId)
	             .orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

	     // Fetch all commissions for the agent from the Commission table
	     List<Commission> commissions = commissionRepository.findByAgent(agent);

	     // Map each Commission entity to a CommissionResponseDto and return as a list
	     return commissions.stream()
	             .map(commission -> {
	                 CommissionResponseDto dto = new CommissionResponseDto();
	                 dto.setCommissionId(commission.getCommissionId());
	                 dto.setCommissionType(commission.getCommissionType());
	                 dto.setIssueDate(commission.getDate());
	                 dto.setAmount(commission.getAmount());
	                 dto.setAgentId(agent.getAgentId());   // Assuming Agent has getAgentId()
	                 dto.setAgentFirstName(agent.getFirstName()); // Assuming Agent has getAgentName()
	                 dto.setAgentLastName(agent.getLastName()); // Assuming Agent has getAgentName()
	                 dto.setPolicyId(commission.getInsurancePolicy().getInsuranceId());
	                 if (commission.getInsurancePolicy().getCustomers() != null && !commission.getInsurancePolicy().getCustomers().isEmpty()) {
	                	    dto.setFirstName(commission.getInsurancePolicy().getCustomers().get(0).getFirstName());
	                	} else {
	                	    // Handle the case where there are no customers
	                	    System.out.println("No customers available for this insurance policy");
	                	}

	                 return dto;
	             })
	             .collect(Collectors.toList());
	 }

	@Override
	  public String agentclaimPolicy(ClaimRequestDto claimRequestDto, Long agentId) {
	      InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
	              .orElseThrow(() -> new RuntimeException("Policy not found"));

	      // Agent's Commission Claim
	      Agent agent = agentRepository.findById(agentId)
	              .orElseThrow(() -> new AgentNotFoundException("Agent not found"));

	      Claim agentClaim = new Claim();
	      agentClaim.setClaimAmount(claimRequestDto.getClaimAmount()); // Use the claim amount provided in the request
	      agentClaim.setBankName(claimRequestDto.getBankName());
	      agentClaim.setBranchName(claimRequestDto.getBranchName());
	      agentClaim.setBankAccountId(claimRequestDto.getBankAccountId());
	      agentClaim.setIfscCode(claimRequestDto.getIfscCode());
	      agentClaim.setClaimedStatus(ClaimStatus.PENDING.name());
	      agentClaim.setPolicy(insurancePolicy);
	      agentClaim.setAgent(agent);  // Set the agent reference

	      claimRepository.save(agentClaim);

	      return "Claim of " + claimRequestDto.getClaimAmount() + " has been successfully created for policy ID "
	              + claimRequestDto.getPolicyId() + ". The claim is pending approval.";
	  }

	public PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<CustomerResponseDto> customerResponseDtos = customerPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                customerResponseDtos,
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements(),
                customerPage.getTotalPages(),
                customerPage.isLast()
        );
    }

    private CustomerResponseDto convertToDto(Customer customer) {
        CustomerResponseDto dto = new CustomerResponseDto();
        BeanUtils.copyProperties(customer, dto);
        if (customer.getCity() != null) {
            dto.setCityName(customer.getCity().getCity_name()); // Adjust according to your City entity
        }
        return dto;
    }

	@Override
	public Agent getAgentByUsername(String username) {
		return agentRepository.findByUserUsername(username);

	}
	
	

	@Override
	public List<Customer> getCustomersByCity(Long id) {
		return customerRepository.findByCityId(id);	}

	@Override
	public Agent getAgentByUserId(Long id) {
		return agentRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("Agent not found for user ID: " + id));

	}

//	@Override
//	public String registerCustomer(RegisterDto registerDto) throws IOException {
////		if(userRepository.existsByUsername(registerDto.getUsername()) || userRepository.existsByEmail(registerDto.getEmail())) {
////			User user = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail()).orElseThrow(()->new AllExceptions.CustomerNotFoundException("This customer is alredy exists"));
////			return String.valueOf(savedCustomer.getCustomerId());
////		}
//		
//		 if (userRepository.existsByUsername(registerDto.getUsername()) || 
//			        userRepository.existsByEmail(registerDto.getEmail())) {
//			        throw new APIException(HttpStatus.BAD_REQUEST, "User already registered with this username or email!");
//			    }
//		
//		
//		if (userRepository.existsByUsername(registerDto.getUsername())) {
//			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
//		}
//
//		if (userRepository.existsByEmail(registerDto.getEmail())) {
//			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
//		}
//
//		// Create a new User entity and set basic details
//		User user = new User();
//		user.setUsername(registerDto.getUsername());
//		user.setEmail(registerDto.getEmail());
//		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//		// Assign roles to the User
//		Set<Role> roles = new HashSet<>();
//		for (String roleName : registerDto.getRoles()) {
//			Role role = roleRepository.findByName(roleName)
//					.orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
////			if (!roleName.equals("ROLE_ADMIN") && !roleName.equals("ROLE_CUSTOMER")) {
////				throw new BankApiException(HttpStatus.BAD_REQUEST, "Only Admins and Customers can self-register");
////			}
//			roles.add(role);
//	}
//		user.setRoles(roles);
//
//		// Save the User entity
//		userRepository.save(user);
//		// Assign roles to the User
//		
//		
//		User savedUser = userRepository.save(user);
//		
//		City city = cityRepository.findById(registerDto.getCityId())
//	            .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));
//
//	    Customer customer = new Customer();
//	  customer.setCustomerId(user.getId());
//	    customer.setUser(user);
//	    customer.setFirstName(registerDto.getFirstName());
//	    customer.setLastName(registerDto.getLastName());
//
//	    if (registerDto.getPhone_number() == null ) {
//	        throw new APIException(HttpStatus.BAD_REQUEST, "Phone number is required");
//	    }
//	    customer.setPhoneNumber(registerDto.getPhone_number());
//
//	    if (registerDto.getDob() == null) {
//	        throw new APIException(HttpStatus.BAD_REQUEST, "Date of birth is required");
//	    }
//	    customer.setDob(registerDto.getDob());
//
//	    customer.setCity(city);
//	    customer.setActive(true);
//	    customer.setVerified(true);
//
//	    customer.setRegistrationDate(LocalDate.now());
//	
//
//	    Customer savedCustomer = customerRepository.save(customer);
//		
//        // Return the customerId
//        return String.valueOf(savedCustomer.getCustomerId());
//		
//}
	//------------------------------
//	@Override
//	public String registerCustomer(RegisterDto registerDto) throws IOException {
//	    // Check if a user with the same username or email already exists
//	    Optional<User> existingUserOptional = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail());
//
//	    if (existingUserOptional.isPresent()) {
//	        User existingUser = existingUserOptional.get();
//	        Optional<Customer> existingCustomerOptional = customerRepository.findByUser(existingUser);
//
//	        if (existingCustomerOptional.isPresent()) {
//	            Customer existingCustomer = existingCustomerOptional.get();
//	            // Return the existing customer's ID
//	            return String.valueOf(existingCustomer.getCustomerId());
//	        } else {
//	            // If the user exists but not as a customer, throw an exception
//	            throw new APIException(HttpStatus.BAD_REQUEST, "User already registered with this username or email, but not as a customer.");
//	        }
//	    }
//
//	    // Create a new User entity and set basic details
//	    User user = new User();
//	    user.setUsername(registerDto.getUsername());
//	    user.setEmail(registerDto.getEmail());
//	    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//	    // Assign roles to the User
//	    Set<Role> roles = new HashSet<>();
//	    for (String roleName : registerDto.getRoles()) {
//	        Role role = roleRepository.findByName(roleName)
//	                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
//	        roles.add(role);
//	    }
//	    user.setRoles(roles);
//
//	    // Save the User entity
//	    User savedUser = userRepository.save(user);
//
//	    // Find the city based on cityId
//	    City city = cityRepository.findById(registerDto.getCityId())
//	            .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));
//
//	    // Create and set up the new Customer entity
//	    Customer customer = new Customer();
//	    customer.setCustomerId(savedUser.getId());
//	    customer.setUser(savedUser);
//	    customer.setFirstName(registerDto.getFirstName());
//	    customer.setLastName(registerDto.getLastName());
//
//	    if (registerDto.getPhone_number() == null) {
//	        throw new APIException(HttpStatus.BAD_REQUEST, "Phone number is required");
//	    }
//	    customer.setPhoneNumber(registerDto.getPhone_number());
//
//	    if (registerDto.getDob() == null) {
//	        throw new APIException(HttpStatus.BAD_REQUEST, "Date of birth is required");
//	    }
//	    customer.setDob(registerDto.getDob());
//
//	    customer.setCity(city);
//	    customer.setActive(true);
//	    customer.setVerified(true);
//	    customer.setRegistrationDate(LocalDate.now());
//
//	    // Save the Customer entity
//	    Customer savedCustomer = customerRepository.save(customer);
//
//	    // Return the new customer's ID
//	    System.out.println("Returning customer ID: " + savedCustomer.getCustomerId());
//	    return String.valueOf(savedCustomer.getCustomerId());
//	}
	//------------------------
	
	
	@Override
	public String registerCustomer(RegisterDto registerDto) throws IOException {
	    // Check if a user with the same username or email already exists
	    Optional<User> existingUserOptional = userRepository.findByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail());

	    // If user exists, check if they are a customer
	    if (existingUserOptional.isPresent()) {
	        User existingUser = existingUserOptional.get();
	        Optional<Customer> existingCustomerOptional = customerRepository.findByUser(existingUser);

	        if (existingCustomerOptional.isPresent()) {
	            Customer existingCustomer = existingCustomerOptional.get();
	            // Return the existing customer's ID if already registered as a customer
	            return String.valueOf(existingCustomer.getCustomerId());
	        } else {
	            // If the user exists but not as a customer, proceed to register them as a customer
	            return registerAsCustomer(existingUser, registerDto);
	        }
	    }

	    // If no user exists, create a new user and register them as a customer
	    return registerNewUserAndCustomer(registerDto);
	}

	// Helper method to register a new user and customer
	private String registerNewUserAndCustomer(RegisterDto registerDto) throws IOException {
	    // Create a new User entity
	    User user = new User();
	    user.setUsername(registerDto.getUsername());
	    user.setEmail(registerDto.getEmail());
	    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

	    // Assign roles to the User
	    Set<Role> roles = new HashSet<>();
	    for (String roleName : registerDto.getRoles()) {
	        Role role = roleRepository.findByName(roleName)
	                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
	        roles.add(role);
	    }
	    user.setRoles(roles);

	    // Save the User entity
	    User savedUser = userRepository.save(user);

	    // Proceed to register as a customer with the saved User
	    return registerAsCustomer(savedUser, registerDto);
	}

	// Helper method to register an existing or new user as a customer
	private String registerAsCustomer(User user, RegisterDto registerDto) throws IOException {
	    // Find the city based on cityId
	    City city = cityRepository.findById(registerDto.getCityId())
	            .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));

	    // Create and set up the new Customer entity
	    Customer customer = new Customer();
	    customer.setCustomerId(user.getId()); // Using User ID as Customer ID
	    customer.setUser(user);
	    customer.setFirstName(registerDto.getFirstName());
	    customer.setLastName(registerDto.getLastName());

	    if (registerDto.getPhone_number() == null) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Phone number is required");
	    }
	    customer.setPhoneNumber(registerDto.getPhone_number());

	    if (registerDto.getDob() == null) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Date of birth is required");
	    }
	    customer.setDob(registerDto.getDob());

	    customer.setCity(city);
	    customer.setActive(true);
	    customer.setVerified(true);
	    customer.setRegistrationDate(LocalDate.now());

	    // Save the Customer entity
	    Customer savedCustomer = customerRepository.save(customer);

	    // Return the new or existing customer's ID
	    return String.valueOf(savedCustomer.getCustomerId());
	}

	
	
//
//	@Override
//    public AgentResponseDto getAgentByName(String firstName, String lastName) {
//		Agent agent = agentRepository.findByUser_FirstNameAndUser_LastName(firstName, lastName)
//	            .orElseThrow(() -> new ResourceNotFoundException("Agent", "name", firstName + " " + lastName));
//
//        return mapToAgentResponseDto(agent);
//    }

    // Method to get agents by active status
//    @Override
//    public List<AgentResponseDto> getAgentsByActiveStatus(boolean active) {
//        List<Agent> agents = agentRepository.findByIsActive(active);
//        return agents.stream()
//                     .map(this::mapToAgentResponseDto)
//                     .collect(Collectors.toList());
//    }
	
	
    // Manual mapping method from Agent entity to AgentResponseDto
    private AgentResponseDto mapToAgentResponseDto(Agent agent) {
        AgentResponseDto dto = new AgentResponseDto();
        dto.setAgentId(agent.getAgentId());
        dto.setFirstName(agent.getFirstName());
        dto.setLastName(agent.getLastName());
        dto.setPhoneNumber(agent.getPhoneNumber());
        dto.setName(agent.getCity().getCity_name());
        dto.setName(agent.getCity().getState().getName());
        dto.setActive(agent.isActive());
//        dto.setTotalCommission(agent.getTotalCommission());
//        dto.setRegistrationDate(agent.getRegistrationDate());
        return dto;
    }

    public PagedResponse<AgentResponseDto> getAgentsByActiveStatus(boolean active, int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Agent> agentsPage = agentRepository.findByIsActive(active, pageable);

        List<AgentResponseDto> agentDtos = agentsPage.getContent().stream()
            .map(this::convertToAgentResponseDto)
            .collect(Collectors.toList());

        return new PagedResponse<>(
                agentDtos,
                agentsPage.getNumber(),
                agentsPage.getSize(),
                agentsPage.getTotalElements(),
                agentsPage.getTotalPages(),
                agentsPage.isLast()
        );
    }
    private AgentResponseDto convertToAgentResponseDto(Agent agent) {
        AgentResponseDto agentDto = new AgentResponseDto();

        // Set basic agent fields
        agentDto.setAgentId(agent.getAgentId());
        agentDto.setFirstName(agent.getFirstName());
        agentDto.setLastName(agent.getLastName());
        agentDto.setPhoneNumber(agent.getPhoneNumber());
        agentDto.setActive(agent.isActive());
       
        // Set city and state if they exist
      


        return agentDto;
    }

	@Override
	public List<AgentResponseDto> getAgentsByActiveStatus(boolean active) {
		
		return null;
	}

	@Override
	public int countActiveAgents() {
		  return agentRepository.countActiveAgents(); 
    }

	@Override
	public double getTotalCommission(Long agentId) {
		  Agent agent = agentRepository.findById(agentId)
		    .orElseThrow(() -> new AgentNotFoundException("Agent not found with id: " + agentId));

		  return agent.getTotalCommission(); 
		 }


	
}
