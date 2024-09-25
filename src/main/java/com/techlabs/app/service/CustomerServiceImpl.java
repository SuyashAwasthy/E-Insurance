package com.techlabs.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.techlabs.app.controller.AdminController;
import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.NomineeDto;
import com.techlabs.app.dto.PaymentDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.DocumentStatus;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.PolicyStatus;
import com.techlabs.app.entity.PremiumType;
import com.techlabs.app.entity.RelationStatus;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SchemeDocument;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AgentNotFoundException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.exception.CustomerNotFoundException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.DocumentRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.repository.KeyValueRepository;
import com.techlabs.app.repository.NomineeRepository;
import com.techlabs.app.repository.PaymentRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.SubmittedDocumentRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

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
	
	@Autowired
	private KeyValueRepository keyValueRepository;
	
	@Autowired
	private SubmittedDocumentRepository submittedDocumentRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	

	
	public CustomerServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			CustomerRepository customerRepository, CityRepository cityRepository, PasswordEncoder passwordEncoder,
			AgentRepository agentRepository, InsuranceSchemeRepository insuranceSchemeRepository,
			InsurancePolicyRepository insurancePolicyRepository, NomineeRepository nomineeRepository,
			ClaimRepository claimRepository, DocumentRepository documentRepository,
			KeyValueRepository keyValueRepository, SubmittedDocumentRepository submittedDocumentRepository,
			PaymentRepository paymentRepository) {
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
		this.documentRepository = documentRepository;
		this.keyValueRepository = keyValueRepository;
		this.submittedDocumentRepository = submittedDocumentRepository;
		this.paymentRepository = paymentRepository;
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
	
	@Override
	  @Transactional
	  public InsurancePolicyDto buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
		
		//Log the customer ID
	    System.out.println("Customer ID received: " + customerId);

	    // Fetch the required entities (InsuranceScheme, Customer, Agent)
	    if (accountRequestDto.getInsuranceSchemeId() == null) {
	        throw new IllegalArgumentException("Insurance Scheme ID must not be null");
	    }

	    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
		        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
		            "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));

	   // System.out.println("Insurance Scheme fetched: " + insuranceScheme);
	    // Check if the insurance scheme is active
	    if (!insuranceScheme.isActive()) {
	        throw new APIException(HttpStatus.FORBIDDEN,
	            "The insurance scheme with ID " + accountRequestDto.getInsuranceSchemeId() + " is not active and cannot be used to buy a policy.");
	    }
	    System.out.println("Insurance Scheme is active");
System.out.println("customer checkinh");
	    Customer customer = customerRepository.findById(customerId)
	        .orElseThrow(() -> new AllExceptions.IdNotFoundException("Sorry, we couldn't find a customer with ID: " + customerId));
	   // System.out.println("Customer fetched: " + customer);

	    if (customer == null) {
	        throw new IllegalArgumentException("Customer ID must not be null");
	    }
	    System.out.println("cusyomerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
	    if (!customer.isActive()) {
	        throw new APIException(HttpStatus.FORBIDDEN, "Customer must be active to buy a policy.");
	    }
	    if (!customer.isVerified()) {
	        throw new APIException(HttpStatus.FORBIDDEN, "Customer must be verified to buy a policy.");
	    }
	    
	    // Calculate the customer's age
	    int customerAge = LocalDate.now().getYear() - customer.getDob().getYear();
	    if (customer.getDob().plusYears(customerAge).isAfter(LocalDate.now())) {
	        customerAge--; // Adjust age if the birthday hasn't occurred yet this year
	    }

	    // Check customer's age against the scheme's age limits
	    if (customerAge < insuranceScheme.getMinimumAge() || customerAge > insuranceScheme.getMaximumAge()) {
	        throw new APIException(HttpStatus.FORBIDDEN,
	            "Customer with ID " + customerId + " does not meet the age requirements for the selected insurance scheme.");
	    }

	    
	 // Check if the customer is verified
	    if (!customer.isVerified()) {
	        throw new APIException(HttpStatus.FORBIDDEN,
	            "Customer with ID " + customerId + " is not verified and cannot purchase a policy.");
	    }
	    
	    


	    Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));
	    
	    System.out.println("/////////////////////////////////////////////");
	    // Create a new InsurancePolicy and set its properties
	    InsurancePolicy insurancePolicy = new InsurancePolicy();
	    insurancePolicy.getCustomers().add(customer); // Set the customer
	    if(agent!=null) {
	    	insurancePolicy.setAgent(agent);
	    	insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
	    }
	    else {
	    	insurancePolicy.setAgent(null);	
	    	insurancePolicy.setRegisteredCommission(0.0);
	    }
	 //   double totalCommission=agent.getTotalCommission() + insuranceScheme.getNewRegistrationCommission();    )
//	    double totalCommission = agent.getTotalCommission() + (double) insuranceScheme.getNewRegistrationCommission();
//agent.setTotalCommission(totalCommission);

	    insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
	    insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
	    insurancePolicy.setIssuedDate(LocalDate.now());
	    insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
	    insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
//	    insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
	    insurancePolicy.setInsuranceScheme(insuranceScheme);
	    insurancePolicy.setPolicyStatus(PolicyStatus.APPROVED.name());
	    

	    // Calculate the sum assured based on the profit ratio
	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
	        + insurancePolicy.getPremiumAmount();
	    insurancePolicy.setClaimAmount(sumAssured);
	  //  insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());
	    insurancePolicy.setVerified(true);

	    // Determine the number of months based on the premium type
	    long months = accountRequestDto.getInstallmentPeriod();
	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
	    insurancePolicy.setInstallmentPayment(installmentAmount);
	    insurancePolicy.setTotalAmountPaid(0.0);

	    // Handle Nominees
	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
	      List<Nominee> nominees = new ArrayList<>();
	      for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
	        Nominee nominee = new Nominee();
	        nominee.setNomineeName(nomineeDto.getNomineeName());
	        nominee.setRelationStatus(nomineeDto.getRelationStatus());
	     //   nominee.setRelationStatus(nomineeDto.getRelationStatus());
	        nomineeRepository.save(nominee); // Save the nominee
	        nominees.add(nominee);
	      }
	      insurancePolicy.setNominees(nominees);
	    } else {
	      throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
	    }

	    // Handle Submitted Documents based on Scheme Documents
	    Set<SchemeDocument> schemeDocuments = insuranceScheme.getSchemeDocuments();
	    if (schemeDocuments != null && !schemeDocuments.isEmpty()) {
	      Set<SubmittedDocument> submittedDocuments = new HashSet<>();
	      for (SchemeDocument schemeDoc : schemeDocuments) {
	        boolean documentFound = false;

	        // Iterate through submitted documents to find a match based on document name
	        for (SubmittedDocumentDto submittedDto : accountRequestDto.getDocuments()) {
	          if (schemeDoc.getName().equalsIgnoreCase(submittedDto.getDocumentName())) {
	            SubmittedDocument submittedDoc = new SubmittedDocument();
	            submittedDoc.setDocumentName(schemeDoc.getName());
	            submittedDoc.setDocumentStatus(DocumentStatus.APPROVED.name()); // Default status
	            submittedDoc.setDocumentImage(submittedDto.getDocumentImage()); // Set the document image from
	                                            // DTOdocumentRepository.save(submittedDoc); // Save the submitted document
	            
	            documentRepository.save(submittedDoc);
	            submittedDocuments.add(submittedDoc);
	            documentFound = true;
	            break;
	          }
	        }

	        // If no matching submitted document is found for a required scheme document,
	        // throw an exception
	        if (!documentFound) {
	          throw new APIException(HttpStatus.BAD_REQUEST,
	              "Document for " + schemeDoc.getName() + " is missing.");
	        }
	      }
	      insurancePolicy.setDocuments(submittedDocuments);
	    } else {
	      throw new APIException(HttpStatus.NOT_FOUND, "No scheme documents available for the selected scheme.");
	    }

	    // Save the insurance policy
//	    insurancePolicyRepository.save(insurancePolicy);
//
//	    // Update the customer's list of policies
//	    customer.getInsurancePolicies().add(insurancePolicy);
//	    customerRepository.save(customer);
//	    
//
//	    return "Policy has been successfully created for customer ID " + customerId + ".";
	 // Save the insurance policy
	    InsurancePolicy savedPolicy = insurancePolicyRepository.save(insurancePolicy);

	    // Add the policy to the customer's list of policies
	    customer.getInsurancePolicies().add(insurancePolicy);
	    customerRepository.save(customer);

	    // Update the agent's total commission
	    double currentCommission = agent.getTotalCommission();
	    agent.setTotalCommission(currentCommission + insuranceScheme.getNewRegistrationCommission());
	    agentRepository.save(agent);

	    // Convert saved policy to DTO
	    InsurancePolicyDto policyDto = convertToDto(savedPolicy);

	    return policyDto;
	  }
	

	@Override
	public InsurancePolicyDto buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {
		System.out.println("///////////////////////////////////////////////////////");
		
		System.out.println(accountRequestDto);
		
		System.out.println("///////////////////////////////////////////////////////");
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
	    System.out.println("cusyomerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
	    if (!customer.isActive()) {
	        throw new APIException(HttpStatus.FORBIDDEN, "Customer must be active to buy a policy.");
	    }
	    if (!customer.isVerified()) {
	        throw new APIException(HttpStatus.FORBIDDEN, "Customer must be verified to buy a policy.");
	    }

	    // Calculate the customer's age
	    int customerAge = LocalDate.now().getYear() - customer.getDob().getYear();
	    if (customer.getDob().plusYears(customerAge).isAfter(LocalDate.now())) {
	        customerAge--; // Adjust age if the birthday hasn't occurred yet this year
	    }

	    // Check customer's age against the scheme's age limits
	    if (customerAge < insuranceScheme.getMinimumAge() || customerAge > insuranceScheme.getMaximumAge()) {
	        throw new APIException(HttpStatus.FORBIDDEN,
	            "Customer with ID " + customerId + " does not meet the age requirements for the selected insurance scheme.");
	    }

	   
	    
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
//	    insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission()); // No commission for policies bought without an agent
	    insurancePolicy.setRegisteredCommission(0); // No commission for policies bought without an agent
	    insurancePolicy.setInsuranceScheme(insuranceScheme);
	    insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());

	    // Calculate the sum assured based on the profit ratio
	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
	        + insurancePolicy.getPremiumAmount();
	   // insurancePolicy.setClaimAmount(sumAssured);
	    insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());

	    // Determine the number of months based on the premium type
	    long months = accountRequestDto.getInstallmentPeriod();
	    // Calculate the total number of months and the installment amount
	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
	    insurancePolicy.setInstallmentPayment(installmentAmount);
	    insurancePolicy.setTotalAmountPaid(0.0);
	    
	    System.out.println("ppp------");
	 // Handle Nominees
	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
	      List<Nominee> nominees = new ArrayList<>();
	      for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
	        Nominee nominee = new Nominee();
	        nominee.setNomineeName(nomineeDto.getNomineeName());
	       // if(nomineeDto.getRelationStatus().equals(RelationStatus.PARENT)) {
	        nominee.setRelationStatus(nomineeDto.getRelationStatus());
	       // }
	       
	        nomineeRepository.save(nominee); // Save the nominee
	        nominees.add(nominee);
	      }
	      insurancePolicy.setNominees(nominees);
	    } else {
	      throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
	    }

	    // Handle Submitted Documents based on Scheme Documents
	    Set<SchemeDocument> schemeDocuments = insuranceScheme.getSchemeDocuments();
	    if (schemeDocuments != null && !schemeDocuments.isEmpty()) {
	      Set<SubmittedDocument> submittedDocuments = new HashSet<>();
	      for (SchemeDocument schemeDoc : schemeDocuments) {
	        boolean documentFound = false;

	        // Iterate through submitted documents to find a match base ]d on document name
	        for (SubmittedDocumentDto submittedDto : accountRequestDto.getDocuments()) {
	          if (schemeDoc.getName().equalsIgnoreCase(submittedDto.getDocumentName())) {
	            SubmittedDocument submittedDoc = new SubmittedDocument();
	            submittedDoc.setDocumentName(schemeDoc.getName());
	            submittedDoc.setDocumentStatus(DocumentStatus.PENDING.name()); // Default status
	            submittedDoc.setDocumentImage(submittedDto.getDocumentImage()); // Set the document image from
	                                            // DTOdocumentRepository.save(submittedDoc); // Save the submitted document
	            
	            documentRepository.save(submittedDoc);
	            submittedDocuments.add(submittedDoc);
	            documentFound = true;
	            break;
	          }
	        }

	        // If no matching submitted document is found for a required scheme document,
	        // throw an exception
	        if (!documentFound) {
	          throw new APIException(HttpStatus.BAD_REQUEST,
	              "Document for " + schemeDoc.getName() + " is missing.");
	        }
	      }
	      insurancePolicy.setDocuments(submittedDocuments);
	    } else {
	      throw new APIException(HttpStatus.NOT_FOUND, "No scheme documents available for the selected scheme.");
	    }

		    InsurancePolicy savedPolicy = insurancePolicyRepository.save(insurancePolicy);

		    customer.getInsurancePolicies().add(insurancePolicy);
		    customerRepository.save(customer);
		    
		    System.out.println("idjhfghdsjkaasjdhfghdjskalksjdhfghdjwkqkwjdhfgbhdjskal");


		    InsurancePolicyDto policyDto = convertToDto(savedPolicy);
		    System.out.println("Policy created with ID: " + insurancePolicy.getInsuranceId());


		    return policyDto;
	
	}
	
	private InsurancePolicyDto convertToDto(InsurancePolicy insurancePolicy) {
	    InsurancePolicyDto dto = new InsurancePolicyDto();
	    dto.setInsuranceId(insurancePolicy.getInsuranceId());
	    dto.setInsuranceSchemeId(insurancePolicy.getInsuranceScheme().getInsuranceSchemeId());
	    dto.setPremiumAmount(insurancePolicy.getPremiumAmount());
	    dto.setPolicyTerm(insurancePolicy.getPolicyTerm());
	    dto.setInstallmentPeriod(insurancePolicy.getInstallmentPeriod());
	    dto.setNominees(insurancePolicy.getNominees().stream().map(nominee -> {
	      NomineeDto nomineeDto = new NomineeDto();
	      nomineeDto.setNomineeName(nominee.getNomineeName());
	      nomineeDto.setRelationStatus(nominee.getRelationStatus());
	      return nomineeDto;
	    }).collect(Collectors.toList()));
	    dto.setDocuments(insurancePolicy.getDocuments().stream().map(document -> {
	      SubmittedDocumentDto documentDto = new SubmittedDocumentDto();
	      documentDto.setDocumentName(document.getDocumentName());
	      documentDto.setDocumentStatus(document.getDocumentStatus());
	      return documentDto;
	    }).collect(Collectors.toSet()));
	    
	    System.out.println("Policy created with ID: " + insurancePolicy.getInsuranceId());

	    return dto;
	  }
		

//	      // Save the insurance policy to the repository
//	      insurancePolicyRepository.save(insurancePolicy);
//
//	      // Update the customer's list of policies
//	      customer.getInsurancePolicies().add(insurancePolicy);
//	      customerRepository.save(customer);
//
//	    // Save the insurance policy to the repository
//	    insurancePolicyRepository.save(insurancePolicy);
//
//	    return "Policy has been successfully created for customer ID " + customerId + " without an agent.";
//	
//	 
//
//}

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

	@Transactional
	  @Override
	  public String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId) {
	      
		if (claimRequestDto == null) {
	        throw new IllegalArgumentException("ClaimRequestDto cannot be null.");
	    }
	    
	    // Extract policyId and ensure it's not null
	    Long policyId = claimRequestDto.getPolicyId();
	    if (policyId == null) {
	        throw new IllegalArgumentException("Policy ID cannot be null.");
	    }
		
		// Find the policy by its ID
	      InsurancePolicy policy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
	              .orElseThrow(() -> new AllExceptions.PolicyNotFoundException("Policy not found"));

	      // Check if the given customer is associated with the policy
	      boolean customerExistsInPolicy = policy.getCustomers().stream()
	              .anyMatch(customer -> customer.getCustomerId() == customerId);

	      if (!customerExistsInPolicy) {
	          throw new AllExceptions.CustomerNotFoundException("Customer is not associated with this policy.");
	      }

	      // Check if a claim already exists for the policy
	      Optional<Claim> existingClaim = claimRepository.findByPolicy(policy);

	      // Log or print for debugging purposes
	      if (existingClaim.isPresent()) {
	          System.out.println("Claim already exists for the policy, updating it...");
	      } else {
	          System.out.println("No existing claim, creating a new one...");
	      }

	      // If a claim exists and the customer wants to cancel, apply the cancellation logic
	      Claim claim;
	      if (existingClaim.isPresent()) {
	          claim = existingClaim.get();
	      } else {
	          // If no claim exists, create a new one
	          claim = new Claim();
	          claim.setPolicy(policy);
	          // Since there's no direct link to the customer, we don't set the customer directly in the claim
	      }

	      // Get the policy amount (assume policy has a premiumAmount or totalAmount field)
	     // double policyAmount = policy.getPremiumAmount();
	      double policyAmount = policy.getTotalAmountPaid();
	      // Calculate the claim amount and apply 20% deduction for cancellation
	      double claimAmount;
	      if (policy.getMaturityDate().isAfter(LocalDate.now())) {
	          // Apply 20% deduction if canceled before maturity
	          double deductionPercentage = Double.parseDouble(keyValueRepository.getValueByKey("deduction_percentage"));
	          double deductionAmount = policyAmount * (deductionPercentage / 100);
	          claimAmount = policyAmount - deductionAmount;

	          // Mark as canceled and set the deduction details
	          claim.setCancel(true);
	          System.out.println("Policy canceled before maturity, applying 20% deduction.");
	      } else {
	          // No deduction if claimed after maturity
	          claimAmount = policyAmount;
	          claim.setCancel(false);  // No cancellation flag after maturity
	          System.out.println("Policy claimed after maturity, no deduction applied.");
	      }

	      // Set claim details from the request
	      claim.setClaimAmount(claimAmount);
	      claim.setBankName(claimRequestDto.getBankName());
	      claim.setBranchName(claimRequestDto.getBranchName());
	      claim.setBankAccountId(claimRequestDto.getBankAccountId());
	      claim.setIfscCode(claimRequestDto.getIfscCode());
	      claim.setClaimedStatus("PENDING");

	      // Save the claim in the repository
	      claimRepository.save(claim);
	      System.out.println("Claim has been saved: " + claim);

	      return "Policy cancellation or claim has been created for the customer.";
	  }



	@Override
	public String buyPolicy(InsurancePolicyDto accountRequestDto, Customer customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buyPolicy(InsurancePolicyDto accountRequestDto, Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void changePassword(Long customerId, ChangePasswordDto changePasswordDto) {
		 Customer customer = customerRepository.findById(customerId) 
		            .orElseThrow(() -> new RuntimeException("Customer not found")); 
		        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), customer.getUser().getPassword())) { 
		            throw new RuntimeException("Incorrect old password"); 
		        } 
		        customer.getUser().setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword())); 
		       customerRepository.save(customer); 
		    
		
	}

	@Override
	public CustomerResponseDto findCustomerByid(long customerId) {
		Customer customer = customerRepository.findById(customerId)
		           .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
		 return convertCustomerToCustomerResponseDto(customer);
	}
	private CustomerResponseDto convertCustomerToCustomerResponseDto(Customer customer) {
		CustomerResponseDto customerDto = new CustomerResponseDto();


		customerDto.setFirstName(customer.getFirstName());

	     customerDto.setLastName(customer.getLastName());
		
		customerDto.setActive(customer.isActive());
		
		//customerDto.setUser(customer.getUser().getEmail());
customerDto.setEmail(customer.getUser().getEmail());
		customerDto.setDob(customer.getDob());
		customerDto.setPhoneNumber(customer.getPhoneNumber());
		customerDto.setCityName(customer.getCity().getCity_name());
		customerDto.setStateName(customer.getCity().getState().getName());


	    return customerDto;
	}

	@Override
	@Transactional
	public void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto) {
		Customer customer = customerRepository.findById(customerId) 
	            .orElseThrow(() -> new CustomerNotFoundException("Customer not found")); 
	        // Update customer fields
		 if(customerRequestDto.getFirstName()!=null)
	customer.setFirstName(customerRequestDto.getFirstName()); 
		 if(customerRequestDto.getLastName()!=null)
	        customer.setLastName(customerRequestDto.getLastName()); 
	        if(customerRequestDto.getIsActive()!=null)
	        customer.setActive(customerRequestDto.getIsActive());
	       
	        customer.setDob(customerRequestDto.getDob());
	      //  if(customerRequestDto.getPhoneNumber()!=null)
	        customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
	     
	        // Set other fields as necessary 
	        customerRepository.save(customer); 

		
	}

	@Override
	public InsurancePolicyDto getPolicyByid(Long customerId, Long policyId) {
		// Fetch the InsurancePolicy entity by customerId and policyId
        InsurancePolicy insurancePolicy = insurancePolicyRepository.findByCustomerIdAndPolicyId(customerId, policyId);
        
        if (insurancePolicy == null) {
            throw new AllExceptions.PolicyNotFoundException("Policy not found for customer ID: " + customerId + " and policy ID: " + policyId);
        }
      

        // Convert the InsurancePolicy entity to InsurancePolicyDto
        InsurancePolicyDto dto = new InsurancePolicyDto();
        dto.setInsuranceId(insurancePolicy.getInsuranceId());
      //  dto.setInsuranceScheme(scheme != null ? scheme.getInsuranceScheme() : null); // Set scheme name
        //dto.setInsuranceId(insurancePolicy.getInsuranceScheme().getInsuranceScheme())  ;
      //  dto.setInsuranceScheme(insurancePolicy.getInsuranceId().getInsuranceScheme());
        dto.setInsuranceSchemeId(insurancePolicy.getInsuranceScheme().getInsuranceSchemeId());
        dto.setAgentId(insurancePolicy.getAgent() != null ? insurancePolicy.getAgent().getAgentId() : null);
        dto.setClaimId(insurancePolicy.getClaim() != null ? insurancePolicy.getClaim().getClaimId() : null);
        dto.setNominees(insurancePolicy.getNominees().stream().map(n -> new NomineeDto(n)).collect(Collectors.toList()));
       // dto.setNomineeIds(insurancePolicy.getNominees().stream().map(Nominee::getNomineeId).collect(Collectors.toList()));
        //dto.setPaymentIds(insurancePolicy.getPayments().stream().map(Payment::getPaymentId).collect(Collectors.toList()));
      //  dto.setDocumentIds(insurancePolicy.getDocuments().stream().map(Document::getDocumentId).collect(Collectors.toSet()));
        dto.setCustomerIds(insurancePolicy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()));
        dto.setIssuedDate(insurancePolicy.getIssuedDate());
        dto.setMaturityDate(insurancePolicy.getMaturityDate());
        dto.setPremiumAmount(insurancePolicy.getPremiumAmount());
        dto.setPolicyStatus(insurancePolicy.getPolicyStatus());
        dto.setActive(insurancePolicy.isActive());
        dto.setPolicyTerm(insurancePolicy.getPolicyTerm());
        dto.setInstallmentPeriod(insurancePolicy.getInstallmentPeriod());
     //   dto.setDocuments(insurancePolicy.getDocuments().stream().map(d -> new SubmittedDocumentDto(d)).collect(Collectors.toList()));

        return dto;
    }

	@Override
	public PagedResponse<InsurancePolicyDto> getAllPoliciesByCustomerId(Long customerId, int page, int size) {
		 Pageable pageable = PageRequest.of(page, size, Sort.by("issuedDate").ascending()); // Adjust sort criteria as needed
		    Page<InsurancePolicy> pagedPolicies = insurancePolicyRepository.findByCustomersCustomerId(customerId, pageable);

//		    List<InsurancePolicyDto> policies = pagedPolicies.getContent().stream().map(policy -> {
//		        InsuranceScheme scheme = policy.getInsuranceScheme();
//		        InsurancePolicyDto dto = new InsurancePolicyDto();
//		        dto.setInsuranceId(policy.getInsuranceId());
//		        dto.setInsuranceSchemeId(scheme != null ? scheme.getInsuranceSchemeId() : null);
//		        dto.setInsuranceScheme(scheme != null ? scheme.getInsuranceScheme() : null); // Set scheme name
//		        dto.setAgentId(policy.getAgent() != null ? policy.getAgent().getAgentId() : null);
//		    //    dto.setClaimId(policy.getClaim() != null ? policy.getClaim().getClaimId() : null);
//		        // Check if claim is not null before accessing its properties
//		        Claim claim = policy.getClaim();
//		        dto.setClaimId(claim != null ? claim.getClaimId() : null);
//		        dto.setClaimedStatus(claim != null ? claim.getClaimedStatus() : null); 
//		        dto.setNominees(policy.getNominees().stream().map(n -> new NomineeDto(n)).collect(Collectors.toList()));
//		        dto.setCustomerIds(policy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()));
//		        dto.setIssuedDate(policy.getIssuedDate());
//		        dto.setMaturityDate(policy.getMaturityDate());
//		        dto.setPremiumAmount(policy.getPremiumAmount());
//		        dto.setPolicyStatus(policy.getPolicyStatus());
//		        dto.setActive(policy.isActive());
//		        dto.setPolicyTerm(policy.getPolicyTerm());
//		        dto.setInstallmentPeriod(policy.getInstallmentPeriod());
//		      dto.setClaimedStatus(policy.getClaim().getClaimedStatus());
//		        //dto.setClaimId(policy.getClaim().getClaimId());
//		        return dto;
//		    }).collect(Collectors.toList());
		    List<InsurancePolicyDto> policies = pagedPolicies.getContent().stream().map(policy -> {
		        InsuranceScheme scheme = policy.getInsuranceScheme();
		        InsurancePolicyDto dto = new InsurancePolicyDto();
		        dto.setInsuranceId(policy.getInsuranceId());
		        dto.setInsuranceSchemeId(scheme != null ? scheme.getInsuranceSchemeId() : null);
		        dto.setInsuranceScheme(scheme != null ? scheme.getInsuranceScheme() : null);
		        dto.setAgentId(policy.getAgent() != null ? policy.getAgent().getAgentId() : null);
		        
		        Claim claim = policy.getClaim();
		        dto.setClaimId(claim != null ? claim.getClaimId() : null);
		        dto.setClaimedStatus(claim != null ? claim.getClaimedStatus() : null); // Safe access

		        dto.setNominees(policy.getNominees().stream().map(NomineeDto::new).collect(Collectors.toList()));
		        dto.setCustomerIds(policy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()));
		        dto.setIssuedDate(policy.getIssuedDate());
		        dto.setMaturityDate(policy.getMaturityDate());
		        dto.setPremiumAmount(policy.getPremiumAmount());
		        dto.setPolicyStatus(policy.getPolicyStatus());
		        dto.setActive(policy.isActive());
		        dto.setPolicyTerm(policy.getPolicyTerm());
		        dto.setInstallmentPeriod(policy.getInstallmentPeriod());

		        // Log policy details
		        System.out.println("Mapping policy: " + dto);

		        return dto;
		    }).collect(Collectors.toList());


		    PagedResponse<InsurancePolicyDto> pagedResponse = new PagedResponse<>();
		    pagedResponse.setContent(policies);
		    pagedResponse.setPage(pagedPolicies.getNumber());
		    pagedResponse.setSize(pagedPolicies.getSize());
		    pagedResponse.setTotalElements(pagedPolicies.getTotalElements());
		    pagedResponse.setTotalPages(pagedPolicies.getTotalPages());
		    pagedResponse.setLast(pagedPolicies.isLast());

		    return pagedResponse;
	}
//	  @Override
//	    public List<Customer> findByCityCityId(Long cityId) {
//	        return customerRepository.findByCityId(cityId);
//	    }

	@Override
	public List<Customer> findByCityCityId(Long cityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer registerCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//    public List<CustomerDTO> findByCityCityId(Long cityId) {
//        List<Customer> customers = customerRepository.findByCityId(cityId);
//        return customers.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    private CustomerDTO convertToDTO(Customer customer) {
//        CustomerDTO dto = new CustomerDTO();
//        dto.setCustomerId(customer.getCustomerId());
//        dto.set(customer.getCustomerName());
//        return dto;
//    }
@Override
@Transactional
	public CustomerDTO getCustomerDetailsByPolicyId(long policyId) {
	    // Fetch the policy with payments and other details
	    InsurancePolicy policy = insurancePolicyRepository.findByIdWithPayments(policyId)
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Policy not found with ID: " + policyId));

	    List<Payment> fpayment = paymentRepository.findByPolicyInsuranceId(policyId);
	    List<Customer> customers = policy.getCustomers();
	    if (customers == null || customers.isEmpty()) {
	      throw new APIException(HttpStatus.NOT_FOUND, "No customers found for policy ID: " + policyId);
	    }
	    System.out.println("////////////////////////////////");

	    // Assuming we pick the first customer
	    Customer customer = customers.get(0);

	    // Map customer entity to CustomerDto
	    CustomerDTO customerDto = new CustomerDTO();
	    customerDto.setCustomerId(customer.getCustomerId());
	    customerDto.setFirstName(customer.getFirstName());
	    customerDto.setLastName(customer.getLastName());
	    customerDto.setEmail(customer.getUser().getEmail());
	    customerDto.setCity(customer.getCity().getCity_name());
	    customerDto.setPhoneNumber(customer.getPhoneNumber());
	    customerDto.setActive(customer.isActive());
System.out.println("-------------------------------------------------------");
	    // Map policy entity to InsurancePolicyDto
	    InsurancePolicyDto policyDto = new InsurancePolicyDto();
	    policyDto.setInsuranceId(policy.getInsuranceId());
	    policyDto.setInsuranceSchemeId(policy.getInsuranceScheme().getInsuranceSchemeId());
	    policyDto.setInsuranceScheme(policy.getInsuranceScheme().getInsuranceScheme());
	    policyDto.setInsurancePlan(policy.getInsuranceScheme().getInsurancePlan().getName());
	    policyDto.setIssuedDate(policy.getIssuedDate());
	    policyDto.setMaturityDate(policy.getMaturityDate());
	    policyDto.setPremiumAmount(policy.getPremiumAmount());
	    policyDto.setProfitRatio(policy.getInsuranceScheme().getProfitRatio());
	    policyDto.setAmount(policy.getClaimAmount());
	    policyDto.setPolicyTerm(policy.getPolicyTerm());
	    policyDto.setInstallmentPeriod(policy.getInstallmentPeriod());
	    policyDto.setAgentId(policy.getAgent() != null ? policy.getAgent().getAgentId() : 0);
System.out.println("/////////////////////////////////////////////////");
	    // Calculate Installment Details
	    List<PaymentDto> paymentDtos = new ArrayList<>();
	    int totalInstallments = policy.getPolicyTerm() * 12 / policy.getInstallmentPeriod();
	    LocalDate installmentStartDate = policy.getIssuedDate();
	    double installmentAmount = policy.getPremiumAmount() / totalInstallments; // Calculate installment amount

//	    // Create a map of payments made for this policy based on installment date
//	    Map<LocalDate, Payment> paymentMap = fpayment.stream()
//	        .collect(Collectors.toMap(payment -> payment.getPaymentDate().toLocalDate(), payment -> payment,
//	            (existing, replacement) -> replacement));
	    
	 // Assuming fpayment is your List<Payment>
	    Map<LocalDate, Payment> paymentMap = fpayment.stream()
	        .collect(Collectors.toMap(
	            payment -> payment.getPaymentDate().toLocalDate(), // Key mapper
	            payment -> payment,                                 // Value mapper
	            (existing, replacement) -> replacement              // Merge function
	        ));


//	      System.out.println("-------------------------------------------------------------------->---------->");
//	      System.out.println(fpayment);
//	      System.out.println("-------------------------------------------------------------------->---------->");

	    int n = fpayment.size();
	    for (int i = 1; i <= totalInstallments; i++) {
	      PaymentDto paymentDto = new PaymentDto();
	      paymentDto.setInstallmentNumber(i);
	      LocalDate installmentDate = installmentStartDate.plusMonths((long) policy.getInstallmentPeriod() * (i - 1));
	      paymentDto.setInstallmentDate(installmentDate);
	      paymentDto.setInstallmentAmount(installmentAmount); // Set the calculated installment amount
	      paymentDto.setPaymentStatus("Unpaid"); // Default status

	      // Check if the payment has been made
	      // Payment payment = paymentMap.get(installmentDate);
	      if (i - 1 < n) {
	        paymentDto.setPaymentStatus(fpayment.get(i - 1).getPaymentStatus());
	        paymentDto.setPaidDate(fpayment.get(i - 1).getPaymentDate());
	      }

	      paymentDtos.add(paymentDto);
	    }

	    policyDto.setPayments(paymentDtos);

	    // Set the policyDto in customerDto's insurancePolicies list
	    customerDto.setInsurancePolicies(Collections.singletonList(policyDto));

	    return customerDto;

	}

@Override
public CustomerDTO getCustomerDetailsByCustomerId(Long customerId) {
	// Fetch the customer using the customerId
    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId));

    // Map customer entity to CustomerDTO
    CustomerDTO customerDto = new CustomerDTO();
    customerDto.setCustomerId(customer.getCustomerId());
    customerDto.setFirstName(customer.getFirstName());
    customerDto.setLastName(customer.getLastName());
    customerDto.setEmail(customer.getUser().getEmail());
    customerDto.setCity(customer.getCity().getCity_name());
    customerDto.setPhoneNumber(customer.getPhoneNumber());
    customerDto.setActive(customer.isActive());

    // Fetch the customer's policies and map them to DTOs
    List<InsurancePolicyDto> policyDtos = customer.getInsurancePolicies().stream().map(policy -> {
        InsurancePolicyDto policyDto = new InsurancePolicyDto();
        policyDto.setInsuranceId(policy.getInsuranceId());
        policyDto.setInsuranceSchemeId(policy.getInsuranceScheme().getInsuranceSchemeId());
        policyDto.setInsuranceScheme(policy.getInsuranceScheme().getInsuranceScheme());
        policyDto.setInsurancePlan(policy.getInsuranceScheme().getInsurancePlan().getName());
        policyDto.setIssuedDate(policy.getIssuedDate());
        policyDto.setMaturityDate(policy.getMaturityDate());
        policyDto.setPremiumAmount(policy.getPremiumAmount());
        policyDto.setProfitRatio(policy.getInsuranceScheme().getProfitRatio());
        policyDto.setAmount(policy.getClaimAmount());
        policyDto.setPolicyTerm(policy.getPolicyTerm());
        policyDto.setInstallmentPeriod(policy.getInstallmentPeriod());
        policyDto.setAgentId(policy.getAgent() != null ? policy.getAgent().getAgentId() : 0);
        policyDto.setClaimAmount(policy.getClaimAmount());

        // Fetch payments (installments) and set them to policyDto
        List<PaymentDto> paymentDtos = policy.getPayments().stream().map(payment -> {
            PaymentDto paymentDto = new PaymentDto();
           // paymentDto.setInstallmentNumber(payment.getInstallmentNumber());
            paymentDto.setInstallmentDate(payment.getPaymentDate().toLocalDate());
          //  paymentDto.setInstallmentAmount(payment.getAmount());
            paymentDto.setPaymentStatus(payment.getPaymentStatus());
            paymentDto.setPaidDate(payment.getPaymentDate());
         //   paymentDto.setPaymentMethodId(payment.get)
            return paymentDto;
        }).collect(Collectors.toList());

        policyDto.setPayments(paymentDtos);
        return policyDto;
    }).collect(Collectors.toList());

    // Set policies to customerDto
    customerDto.setInsurancePolicies(policyDtos);

    return customerDto;
}

@Override
public List<Payment> getPaymentsByPolicyId(Long policyId) {
	return paymentRepository.findByPolicyInsuranceId(policyId);
}

@Override
public List<Payment> getPaymentsByCustomerAndPolicy(Long customerId, Long policyId) {
	 // Verify that the policy belongs to the customer
    boolean policyExistsForCustomer = insurancePolicyRepository.existsByCustomerIdAndInsuranceId(customerId, policyId);
    
    if (!policyExistsForCustomer) {
        throw new IllegalArgumentException("No such policy found for this customer.");
    }

    // Fetch payments for the policy
    return paymentRepository.findByPolicyInsuranceId(policyId);
}

public CustomerResponseDto getCustomerById(long customerId) {
    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    return convertToDto(customer);
}

public PagedResponse<CustomerResponseDto> searchCustomersByName(String name, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Customer> customerPage = customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name, pageable);
    
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

public PagedResponse<CustomerResponseDto> searchCustomersByActiveStatus(boolean active, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Customer> customerPage = customerRepository.findByIsActive(active, pageable);
    
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

//@Override
//public List<PaymentDto> getInstallmentsByPolicyId(Long policyId) {
//    // Fetch the policy by ID
//    InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
//            .orElseThrow(() -> new RuntimeException("Policy not found"));
//
//    // Calculate the total number of installments
//    int totalInstallments = policy.getPolicyTerm() * 12 / policy.getInstallmentPeriod();
//
//    // Initialize a list to hold the installments
//    List<PaymentDto> installmentDtos = new ArrayList<>();
//
//    // Get the list of payments associated with the policy
//    List<Payment> payments = policy.getPayments();
//
//    for (int i = 1; i <= totalInstallments; i++) {
//        PaymentDto dto = new PaymentDto();
//        dto.setInstallmentNumber(i);
//        dto.setInstallmentDate(policy.getIssuedDate().plusMonths(policy.getInstallmentPeriod() * (i - 1)));
//
//        // Check if the payment exists for this installment number
//        if (i <= payments.size()) {
//            Payment payment = payments.get(i - 1);
//            dto.setInstallmentAmount(payment.getAmount());
//            dto.setPaymentStatus(payment.getPaymentStatus());
//            dto.setPaidDate(payment.getPaymentDate().toLocalDate());
//        } else {
//            // If payment does not exist for this installment number, set default values
//            dto.setInstallmentAmount(policy.getInstallmentPayment());
//            dto.setPaymentStatus("UNPAID");
//            dto.setPaidDate(null);
//        }

//        installmentDtos.add(dto);
//    }

//    return installmentDtos;
//}

//@Transactional
//@Override
//public String processClaim(ClaimRequestDto claimRequestDto, Long customerId) {
//    if (claimRequestDto == null) {
//        throw new IllegalArgumentException("ClaimRequestDto cannot be null.");
//    }
//
//    Long policyId = claimRequestDto.getPolicyId();
//    if (policyId == null) {
//        throw new IllegalArgumentException("Policy ID cannot be null.");
//    }
//
//    // Fetch the policy by ID
//    InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
//            .orElseThrow(() -> new RuntimeException("Policy not found"));
//
// // Check if the given customer is associated with the policy
//    boolean customerExistsInPolicy = policy.getCustomers().stream()
//            .anyMatch(customer -> customer.getCustomerId() == customerId);
//
//
//    if (!customerExistsInPolicy) {
//        throw new AllExceptions.CustomerNotFoundException("Customer is not associated with this policy.");
//    }
//
//    // Check if a claim already exists for the policy
//    Optional<Claim> existingClaim = claimRepository.findByPolicy(policy);
//
//    Claim claim;
//    if (existingClaim.isPresent()) {
//        claim = existingClaim.get();
//    } else {
//        claim = new Claim();
//        claim.setPolicy(policy);
//    }
//
//    // Determine if the policy is mature
//    boolean isMature = policy.getMaturityDate().isBefore(LocalDate.now());
//
//    // Handle claims based on maturity
//    double policyAmount = policy.getTotalAmountPaid();
//    double claimAmount;
//
//    if (isMature) {
//        // Mature policy: full claim amount
//        claimAmount = policyAmount;
//        claim.setCancel(false);
//        System.out.println("Policy matured, full claim amount is available.");
//    } else {
//        // Before maturity: apply deductions
//        double deductionPercentage = Double.parseDouble(keyValueRepository.getValueByKey("deduction_percentage"));
//        double deductionAmount = policyAmount * (deductionPercentage / 100);
//        claimAmount = policyAmount - deductionAmount;
//        claim.setCancel(true);
//        System.out.println("Policy canceled before maturity, applying deductions.");
//    }
//
//    // Set claim details from the request
//    claim.setClaimAmount(claimAmount);
//    claim.setBankName(claimRequestDto.getBankName());
//    claim.setBranchName(claimRequestDto.getBranchName());
//    claim.setBankAccountId(claimRequestDto.getBankAccountId());
//    claim.setIfscCode(claimRequestDto.getIfscCode());
//    claim.setClaimedStatus("PENDING");
//    
// // Create or retrieve the claim
//     claim = existingClaim.orElse(new Claim());
//    claim.setPolicy(policy);
//    claim.setClaimedStatus("PENDING"); // Set status to PENDING
//
//    // Assign the agent handling the policy to the claim
//    claim.setAgent(policy.getAgent());
//
//    // Save the claim in the repository
//    claimRepository.save(claim);
//    System.out.println("Claim has been saved: " + claim);
//
//    return "Claim has been successfully created for policy ID " + claimRequestDto.getPolicyId();
//}

//@Transactional
//public ClaimResponseDto processClaim(ClaimRequestDto claimRequestDto, Long customerId) {
//	System.out.println(claimRequestDto);
//    if (claimRequestDto == null) {
//        throw new IllegalArgumentException("ClaimRequestDto cannot be null.");
//    }
//System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
//    Long policyId = claimRequestDto.getPolicyId();
//    if (policyId == null) {
//        throw new IllegalArgumentException("Policy ID cannot be null.");
//    }
//
//    // Fetch the policy by ID
//    InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
//            .orElseThrow(() -> new RuntimeException("Policy not found"));
//
//    // Check if the given customer is associated with the policy
//    boolean customerExistsInPolicy = policy.getCustomers().stream()
//            .anyMatch(customer -> customer.getCustomerId() == customerId);
//    
//    if (!customerExistsInPolicy) {
//        throw new AllExceptions.CustomerNotFoundException("Customer is not associated with this policy.");
//    }
//
//    // Check if the policy has matured
//    boolean isMature = policy.getMaturityDate().isBefore(LocalDate.now());
//    double claimAmount;
//    
//
//    if (isMature) {
//        // Mature policy: full claim amount can be claimed
//        claimAmount = policy.getTotalAmountPaid();
//        
//        System.out.println("Policy matured, full claim amount is available.");
//    } else {
//        // Before maturity: apply deductions
//        double deductionPercentage = Double.parseDouble(keyValueRepository.getValueByKey("deduction_percentage"));
//        double deductionAmount = policy.getTotalAmountPaid() * (deductionPercentage / 100);
//        claimAmount = policy.getTotalAmountPaid() - deductionAmount;
//        System.out.println("Policy canceled before maturity, applying deductions.");
//    }
//
//    // Create a new claim and set status to PENDING
//    Claim claim = new Claim();
//    claim.setPolicy(policy);
//    claim.setClaimAmount(claimAmount);
//    claim.setClaimedStatus("PENDING");
//    claim.setBankName(claimRequestDto.getBankName());
//    claim.setBranchName(claimRequestDto.getBranchName());
//    claim.setBankAccountId(claimRequestDto.getBankAccountId());
//    claim.setIfscCode(claimRequestDto.getIfscCode());
////claim.setClaimAmount(claimRequestDto.getClaimAmount());
//   
//    claim.setClaimAmount(policy.getTotalAmountPaid());
//    // Save the claim in the repository
//    claimRepository.save(claim);
//
//    // Return the response
//    ClaimResponseDto responseDto = new ClaimResponseDto();
//    responseDto.setClaimedStatus(true); // Indicating the claim request was successful
//    return responseDto;
//}

@Transactional
public ClaimResponseDto processClaim(ClaimRequestDto claimRequestDto, Long customerId) {
    System.out.println(claimRequestDto);
    if (claimRequestDto == null) {
        throw new IllegalArgumentException("ClaimRequestDto cannot be null.");
    }

    Long policyId = claimRequestDto.getPolicyId();
    if (policyId == null) {
        throw new IllegalArgumentException("Policy ID cannot be null.");
    }

    // Fetch the policy by ID
    InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
            .orElseThrow(() -> new RuntimeException("Policy not found"));

    // Check if the given customer is associated with the policy
    boolean customerExistsInPolicy = policy.getCustomers().stream()
            .anyMatch(customer -> customer.getCustomerId() == customerId);
    
    if (!customerExistsInPolicy) {
        throw new AllExceptions.CustomerNotFoundException("Customer is not associated with this policy.");
    }

    // Check if the policy has matured
    boolean isMature = policy.getMaturityDate().isBefore(LocalDate.now());
    double claimAmount;
    
    if (isMature) {
        claimAmount = policy.getTotalAmountPaid();
        System.out.println("Policy matured, full claim amount is available.");
    } else {
        double deductionPercentage = Double.parseDouble(keyValueRepository.getValueByKey("deduction_percentage"));
        double deductionAmount = policy.getTotalAmountPaid() * (deductionPercentage / 100);
        claimAmount = policy.getTotalAmountPaid() - deductionAmount;
        System.out.println("Policy canceled before maturity, applying deductions.");
    }

    // Create a new claim and set status to PENDING
    Claim claim = new Claim();
    claim.setPolicy(policy);
    claim.setClaimAmount(claimAmount);
    claim.setClaimedStatus("PENDING");
    claim.setBankName(claimRequestDto.getBankName());
    claim.setBranchName(claimRequestDto.getBranchName());
    claim.setBankAccountId(claimRequestDto.getBankAccountId());
    claim.setIfscCode(claimRequestDto.getIfscCode());

    // Save the claim first
    claim = claimRepository.save(claim);

    // Set the claim in the policy
    policy.setClaim(claim); // Link the claim to the policy
    insurancePolicyRepository.save(policy); // Save the policy with the claim

    // Return the response
    ClaimResponseDto responseDto = new ClaimResponseDto();
    responseDto.setClaimedStatus(true); // Indicating the claim request was successful
    return responseDto;
}



public CustomerDTO checkCustomerStatus(String email) {
	 Customer customer = customerRepository.findByUserEmail(email)
		        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found"));

    return new CustomerDTO(customer.getCustomerId(), customer.isActive());
}

//@Override
//public List<CustomerResponseDto> getAllCustomerByAgent() {
//    // 1. Get the current agent ID
//    Long agentId = getAgentDetails().getAgentId(); // Implement logic to get current agent's ID
//
//    // 2. Fetch all insurance policies for the agent
//    List<InsurancePolicy> policies = insurancePolicyRepository.findByAgent_AgentId(agentId);
//
//    // 3. Extract customers from policies and avoid duplicates using a Set
//    Set<Customer> customers = new HashSet<>();
//    for (InsurancePolicy policy : policies) {
//        // Add the customer of the policy to the set
//        customers.addAll(policy.getCustomers());
//    }
//
//    // 4. Convert the Set of Customer entities to a list of CustomerResponseDto
//    List<CustomerResponseDto> customerResponseDtos = customers.stream()
//        .map(this::convertCustomerToCustomerDto) // Correctly use the method
//        .collect(Collectors.toList());
//
//    return customerResponseDtos;
//}
@Override
public PagedResponse<CustomerResponseDto> getAllCustomerByAgent(int page, int size) {
    Long agentId = getAgentDetails().getAgentId();
    List<InsurancePolicy> policies = insurancePolicyRepository.findByAgent_AgentId(agentId);

    Set<Customer> customersSet = new HashSet<>();
    for (InsurancePolicy policy : policies) {
        customersSet.addAll(policy.getCustomers());
    }

    List<Customer> customersList = new ArrayList<>(customersSet);
    int totalElements = customersList.size();

    // Calculate pagination
    int start = Math.min(page * size, totalElements);
    int end = Math.min(start + size, totalElements);
    List<Customer> pagedCustomers = customersList.subList(start, end);

    List<CustomerResponseDto> customerResponseDtos = pagedCustomers.stream()
        .map(this::convertCustomerToCustomerDto)
        .collect(Collectors.toList());

    int totalPages = (int) Math.ceil((double) totalElements / size);
    boolean last = page + 1 >= totalPages;

    return new PagedResponse<>(customerResponseDtos, page + 1, size, totalElements, totalPages, last);
}


private CustomerResponseDto convertCustomerToCustomerDto(Customer customer) {
    CustomerResponseDto customerResponseDto = new CustomerResponseDto();
    customerResponseDto.setCustomerId(customer.getCustomerId());
    customerResponseDto.setFirstName(customer.getFirstName());
    customerResponseDto.setLastName(customer.getLastName());
    customerResponseDto.setDob(customer.getDob());
    customerResponseDto.setPhoneNumber(customer.getPhoneNumber());
    customerResponseDto.setCityName(customer.getCity().getCity_name());
    customerResponseDto.setVerified(customer.isVerified());
    customerResponseDto.setRegistrationDate(customer.getRegistrationDate());
  //  customerResponseDto.setStateName(customer.getAddress().getState().getName());
    customerResponseDto.setEmail(customer.getUser().getEmail());
    customerResponseDto.setActive(customer.isActive());

    return customerResponseDto; // Return the created DTO
}

//private Agent getAgentDetails() {
//	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//	     UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//	     return agentRepository.findByUser(userRepository
//	       .findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
//	       .orElseThrow(() -> new AllExceptions.UserNotFoundException("User not found")));
//	    }
//	    throw new AgentNotFoundException("agent not found");
//	 }


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


public Customer findCustomerByEmail(String email) {
    // Find the user by email first
    Optional<User> userOptional = userRepository.findByEmail(email);
    
    // If no user is found, return null
    if (!userOptional.isPresent()) {
        return null;
    }

    User user = userOptional.get();

    // Find customer by the associated user
    Optional<Customer> customerOptional = customerRepository.findByUser(user);
    
    // Return customer if found, otherwise null
    return customerOptional.orElse(null);
}


   

}

	

