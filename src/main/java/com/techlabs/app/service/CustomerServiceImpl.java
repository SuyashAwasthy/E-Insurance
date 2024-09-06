package com.techlabs.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.NomineeDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.PolicyStatus;
import com.techlabs.app.entity.PremiumType;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.DocumentRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.repository.NomineeRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private InsuranceSchemeRepository insuranceSchemeRepository;

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;
	
	@Autowired
	private NomineeRepository nomineeRepository;
	
	@Autowired
	private ClaimRepository claimRepository;
	
	@Autowired
private DocumentRepository documentRepository;
	

	public CustomerServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			CustomerRepository customerRepository, CityRepository cityRepository, PasswordEncoder passwordEncoder,
			AgentRepository agentRepository, InsuranceSchemeRepository insuranceSchemeRepository,
			InsurancePolicyRepository insurancePolicyRepository, NomineeRepository nomineeRepository,
			ClaimRepository claimRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.customerRepository = customerRepository;
		this.cityRepository = cityRepository;
		this.passwordEncoder = passwordEncoder;
		this.agentRepository = agentRepository;
		this.insuranceSchemeRepository = insuranceSchemeRepository;
		this.insurancePolicyRepository = insurancePolicyRepository;
		this.nomineeRepository = nomineeRepository;
		this.claimRepository = claimRepository;
	}

	@Override
	@Transactional

	public void addCustomer(RegisterDto registerDto) {
		// Check if the username or email already exists
		if (userRepository.existsByUsername(registerDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}

		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		// Create a new user
		User user = new User();
		user.setUsername(registerDto.getUsername());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		// Assign customer role to the user
		Set<Role> roles = new HashSet<>();
		Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
				.orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Customer role not found"));
		roles.add(customerRole);
		user.setRoles(roles);

		// Save user to the repository
		userRepository.save(user);

		// Find the city using cityId
		City city = cityRepository.findById(registerDto.getCityId())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));

		// Create a new customer
		Customer customer = new Customer();
		customer.setUser(user);
		customer.setFirstName(registerDto.getFirstName());
		customer.setLastName(registerDto.getLastName());
		customer.setPhoneNumber(registerDto.getPhone_number());
		customer.setDob(registerDto.getDob());
		customer.setCity(city);
		customer.setActive(true); // Set default status as active
		customer.setVerified(false); // Set default verification status

		// Save customer to the repository
		customerRepository.save(customer);
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

//	@Override
//	public String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	

	@Override
	public String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
		// Create a new InsurancePolicy entity
		InsurancePolicy policy = new InsurancePolicy();

		// Map basic fields from the DTO to the entity
		policy.setIssuedDate(insurancePolicyDto.getIssuedDate());
		policy.setMaturityDate(insurancePolicyDto.getMaturityDate());
		policy.setPremiumAmount(insurancePolicyDto.getPremiumAmount());
		policy.setPolicyStatus(insurancePolicyDto.getPolicyStatus());
	//	policy.setActive(insurancePolicyDto.isActive());

		// Fetch and set associated InsuranceScheme entity
		InsuranceScheme scheme = insuranceSchemeRepository.findById(insurancePolicyDto.getInsuranceSchemeId())
				.orElseThrow(() -> new RuntimeException(
						"Insurance Scheme not found for ID: " + insurancePolicyDto.getInsuranceSchemeId()));
		policy.setInsuranceScheme(scheme);

		// Fetch and set associated Agent entity
		Agent agent = agentRepository.findById(insurancePolicyDto.getAgentId())
				.orElseThrow(() -> new RuntimeException("Agent not found for ID: " + insurancePolicyDto.getAgentId()));
		policy.setAgent(agent);

		insurancePolicyRepository.save(policy);

		return "Insurance Policy created successfully.";
	}
//
//	@Override
//	public String buyPolicy(PolicyAccountRequestDto accountRequestDto, long customerId) {
//		System.out.println("Received PolicyAccountRequestDto: " + accountRequestDto);
//		 if (accountRequestDto.getInsuranceSchemeId() == null) {
//		        throw new IllegalArgumentException("Insurance scheme ID must not be null");
//		    }
//		    if (accountRequestDto.getAgentId() == null) {
//		        throw new IllegalArgumentException("Agent ID must not be null");
//		    }
//		
////		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(null).orElseThrow(() -> new ResourceNotFoundException(
////				"Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
//		    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
//		            .orElseThrow(() -> new ResourceNotFoundException(
//		                "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
//		    System.out.println("gagdgdhdhhhhhhhhhhhhuwysh");
//		   System.out.println(insuranceScheme);
//		Customer customer = customerRepository.findById(customerId)
//				.orElseThrow(() -> new ResourceNotFoundException("Sorry, we couldn't find a customer with ID: " + customerId));
//		Agent agent = agentRepository.findById(accountRequestDto.getAgentId()).orElseThrow(
//				() -> new ResourceNotFoundException("Sorry, we couldn't find a agent with ID: " + accountRequestDto.getAgentId()));
//		InsurancePolicy policyAccount = new InsurancePolicy();
//		policyAccount.setCustomer(customer);
//		policyAccount.setAgent(agent);
//
////	      
////	      private Double installmentAmount;
////	      
////	      private Double totalPaidAmount;
//		policyAccount.setPremiumType(accountRequestDto.getPremiumType());
//		policyAccount.setPolicyTerm(accountRequestDto.getPolicyTerm());
//		policyAccount.setPremiumAmount(accountRequestDto.getPremiumAmount());
//		policyAccount.setMaturityDate(policyAccount.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
//		policyAccount.setSumAssured((policyAccount.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
//				+ policyAccount.getPremiumAmount());
//		long months;
//		if (policyAccount.getPremiumType().equals(PremiumType.MONTHLY)) {
//			months = 1;
//		} else if (policyAccount.getPremiumType().equals(PremiumType.QUARTERLY)) {
//			months = 3;
//		} else if (policyAccount.getPremiumType().equals(PremiumType.HALF_YEARLY)) {
//			months = 6;
//		} else {
//			months = 12;
//		}
//		double amount = policyAccount.getPremiumAmount();
//		long totalMonths = (policyAccount.getPolicyTerm() * 12) / months;
//		policyAccount.setInstallmentBalance(amount / totalMonths);
//		policyAccount.setClaimAmount(0.0);
//		return "Query has been successfully created for customer ID " + customerId + ".";
//	}

	@Override
	public String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
		
		if (accountRequestDto.getInsuranceSchemeId() == null) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Insurance scheme ID must not be null");
	    }

	    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));

	    Customer customer = customerRepository.findById(customerId)
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a customer with ID: " + customerId));

	    Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));

	    // Create a new InsurancePolicy and set its properties
	    InsurancePolicy insurancePolicy = new InsurancePolicy();

	    insurancePolicy.getCustomers().add(customer);
	    
	    insurancePolicy.setAgent(agent);

	    insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
	    insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
	    insurancePolicy.setIssuedDate(LocalDate.now()); // Assuming issued date is set to the current date
	    insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
	    insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
	    insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
	    insurancePolicy.setInsuranceScheme(insuranceScheme);
	    insurancePolicy.setActive(true);
	    
	    //handle nominees
	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
	    	System.out.println("checking for nominees---------------------------------------------------");
	        List<Nominee> nominees = new ArrayList<>();
	    //    for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
	        for(NomineeDto nomineeDto:accountRequestDto.getNominees()) {
	            Nominee nominee = new Nominee();
	            nominee.setNomineeName(nomineeDto.getNomineeName());
	            nominee.setRelationStatus(nomineeDto.getRelationStatus());
	            
	           nominee =nomineeRepository.save(nominee);
	            nominees.add(nominee);
	            
	        	//InsuranceScheme nominee=nomineeRepository.findById(nomineeId).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"nominee not found withID: "+nomineeId));
	        }
	        insurancePolicy.setNominees(nominees);
	        System.out.println(nominees);// Add nominees to the policy
	    }
	    
	    double totalCommission = agent.getTotalCommission() + insurancePolicy.getRegisteredCommission();
	    agent.setTotalCommission(totalCommission);
	    
	    // Calculate the sum assured based on the profit ratio
	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
	        + insurancePolicy.getPremiumAmount();
	    insurancePolicy.setClaimAmount(sumAssured);
	    insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());

	    // Determine the number of months based on the premium type
	    long months= accountRequestDto.getInstallmentPeriod();
	// Calculate the total number of months and the installment amount
	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
	    insurancePolicy.setInstallmentPayment(installmentAmount);
	    insurancePolicy.setTotalAmountPaid(0.0);
	    
	 // Handle Documents
	      if (accountRequestDto.getDocuments() != null && !accountRequestDto.getDocuments().isEmpty()) {
	          Set<SubmittedDocument> documents = new HashSet<>();
	          for (SubmittedDocumentDto documentDto : accountRequestDto.getDocuments()) {
	              SubmittedDocument document = new SubmittedDocument();
	              document.setDocumentName(documentDto.getDocumentName());
	              document.setDocumentStatus(documentDto.getDocumentStatus()); // Setting document status
	              document.setDocumentImage(documentDto.getDocumentImage()); // Setting document image
	              documentRepository.save(document); // Save the new document to the repository
	              documents.add(document);
	          }
	          insurancePolicy.setDocuments(documents);
	      } else {
	          throw new APIException(HttpStatus.NOT_FOUND, "No documents provided in the request");
	      }

	      // Save the insurance policy to the repository
	      insurancePolicyRepository.save(insurancePolicy);

	      // Update the customer's list of policies
	      customer.getInsurancePolicies().add(insurancePolicy);
	      customerRepository.save(customer);

	    // Save the insurance policy to the repository
	    insurancePolicyRepository.save(insurancePolicy);

	    return "Policy has been successfully created for customer ID " + customerId + ".";
	  }

	@Override
	public String buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {
		System.out.println("c---------------------------------------------------");
	    if (accountRequestDto.getInsuranceSchemeId() == null) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Insurance scheme ID must not be null");
	    }

	    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));

	    Customer customer = customerRepository.findById(customerId)
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a customer with ID: " + customerId));

	    // Create a new InsurancePolicy and set its properties
	    InsurancePolicy insurancePolicy = new InsurancePolicy();

	    insurancePolicy.getCustomers().add(customer);

	    // Since there's no agent, set a default or null value for agent
	    insurancePolicy.setAgent(null);

	    insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
	    insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
	    insurancePolicy.setIssuedDate(LocalDate.now()); // Assuming issued date is set to the current date
	    insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
	    insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
	    insurancePolicy.setRegisteredCommission(0.0); // No commission for policies bought without an agent
	    insurancePolicy.setInsuranceScheme(insuranceScheme);
	    System.out.println("ppp------");
	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
	    	System.out.println("checking for nominees---------------------------------------------------");
	        List<Nominee> nominees = new ArrayList<>();
	    //    for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
	        for(NomineeDto nomineeDto:accountRequestDto.getNominees()) {
	            Nominee nominee = new Nominee();
	            nominee.setNomineeName(nomineeDto.getNomineeName());
	            nominee.setRelationStatus(nomineeDto.getRelationStatus());
	            
	           nominee =nomineeRepository.save(nominee);
	            nominees.add(nominee);
	            
	        	//InsuranceScheme nominee=nomineeRepository.findById(nomineeId).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"nominee not found withID: "+nomineeId));
	        }
	        insurancePolicy.setNominees(nominees);
	        System.out.println(nominees);// Add nominees to the policy
	    }
		   
	    

	    // Calculate the sum assured based on the profit ratio
	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
	        + insurancePolicy.getPremiumAmount();
	    insurancePolicy.setClaimAmount(sumAssured);
	    insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());

	    // Determine the number of months based on the premium type
	    long months = accountRequestDto.getInstallmentPeriod();
	    // Calculate the total number of months and the installment amount
	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
	    insurancePolicy.setInstallmentPayment(installmentAmount);
	    insurancePolicy.setTotalAmountPaid(0.0);
	    
	 // Handle Documents
	      if (accountRequestDto.getDocuments() != null && !accountRequestDto.getDocuments().isEmpty()) {
	          Set<SubmittedDocument> documents = new HashSet<>();
	          for (SubmittedDocumentDto documentDto : accountRequestDto.getDocuments()) {
	              SubmittedDocument document = new SubmittedDocument();
	              document.setDocumentName(documentDto.getDocumentName());
	              document.setDocumentStatus(documentDto.getDocumentStatus()); // Setting document status
	              document.setDocumentImage(documentDto.getDocumentImage()); // Setting document image
	              documentRepository.save(document); // Save the new document to the repository
	              documents.add(document);
	          }
	          insurancePolicy.setDocuments(documents);
	      } else {
	          throw new APIException(HttpStatus.NOT_FOUND, "No documents provided in the request");
	      }

	      // Save the insurance policy to the repository
	      insurancePolicyRepository.save(insurancePolicy);

	      // Update the customer's list of policies
	      customer.getInsurancePolicies().add(insurancePolicy);
	      customerRepository.save(customer);

	    // Save the insurance policy to the repository
	    insurancePolicyRepository.save(insurancePolicy);

	    return "Policy has been successfully created for customer ID " + customerId + " without an agent.";
	
	 

}

	@Override
	public String claimPolicy(ClaimRequestDto claimRequestDto, long customerId) {
		
	
	        // Fetch the policy by ID
	        InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
	                .orElseThrow(() -> new RuntimeException("Policy not found"));

	        // Check if the policy has matured
	        if (insurancePolicy.getMaturityDate().isAfter(LocalDate.now())) {
	            throw new RuntimeException("The policy has not yet matured. Claims can only be made after the maturity date.");
	        }

	        // Create a new Claim
	        Claim claim = new Claim();
	        claim.setClaimAmount(insurancePolicy.getClaimAmount()); // Claim amount is the maturity sum
	        claim.setBankName(claimRequestDto.getBankName());
	        claim.setBranchName(claimRequestDto.getBranchName());
	        claim.setBankAccountId(claimRequestDto.getBankAccountId());
	        claim.setIfscCode(claimRequestDto.getIfscCode());
	        claim.setClaimedStatus(ClaimStatus.PENDING.name());
	        claim.setPolicy(insurancePolicy);

	        // Assign the agent handling the policy to the claim
	        claim.setAgent(insurancePolicy.getAgent());

	        // Link the claim to the insurance policy
	        insurancePolicy.setClaim(claim);

	        // Save the claim
	        claimRepository.save(claim);

	        return "Claim has been successfully created for policy ID " + claimRequestDto.getPolicyId();
	}

	@Override
	public String requestPolicyCancellation(CancellationRequestDto cancellationRequest, Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}
}
