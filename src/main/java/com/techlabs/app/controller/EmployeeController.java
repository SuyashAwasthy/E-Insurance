package com.techlabs.app.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.ApiException;
import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.DocumentVerificationDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.dto.VerificationDto;
import com.techlabs.app.dto.VerifiedDocumentDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.DocumentStatus;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.PendingVerification;
import com.techlabs.app.entity.PolicyStatus;
import com.techlabs.app.entity.SchemeDocument;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.PendingVerificationRepository;
import com.techlabs.app.repository.SubmittedDocumentRepository;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.service.EmployeeService;
import com.techlabs.app.service.InsurancePolicyService;
import com.techlabs.app.service.SubmittedDocumentService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/E-Insurance/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
@CrossOrigin(origins="http://localhost:3000")
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	 @Autowired
	    private EmployeeService employeeService;
	 
	 @Autowired
	 private AdminService adminService;
	 
	 @Autowired
	    private CustomerRepository customerRepository;

	    @Autowired
	    private PendingVerificationRepository pendingVerificationRepository;
	    
	    private CustomerService customerService;
	 @Autowired 
	 private InsurancePolicyRepository insurancePolicyRepository;
	 @Autowired
	 private SubmittedDocumentRepository submittedDocumentRepository;
	 
	 @Autowired
	 private SubmittedDocumentService submittedDocumentService;
	 
	 @Autowired
	 private InsurancePolicyService insuranceService;
	    
		public EmployeeController(EmployeeService employeeService, CustomerRepository customerRepository,
			PendingVerificationRepository pendingVerificationRepository, CustomerService customerService,
			InsurancePolicyRepository insurancePolicyRepository,
			SubmittedDocumentRepository submittedDocumentRepository) {
	
		this.employeeService = employeeService;
		this.customerRepository = customerRepository;
		this.pendingVerificationRepository = pendingVerificationRepository;
		this.customerService = customerService;
		this.insurancePolicyRepository = insurancePolicyRepository;
		this.submittedDocumentRepository = submittedDocumentRepository;
	}
		
		  @Operation(summary = "view Employee By ID")
	      @GetMapping("/view-employee-by-id/{employeeId}")
	      public ResponseEntity<EmployeeResponseDto> viewEmployeebyId(@PathVariable(name = "employeeId") long employeeId) {
	      return new ResponseEntity<EmployeeResponseDto>(adminService.findEmployeeByid(employeeId), HttpStatus.OK);
	    }
		
		@PostMapping("/registerAgent")
	    public ResponseEntity<String> registerAgent(@RequestBody @Valid AgentRequestDto agentRequestDto) {
	      System.out.println(agentRequestDto);
	        String response = employeeService.registerAgent(agentRequestDto);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }
		

	      @Operation(summary = "Update Agent")
	      @PutMapping("updateAgent/{agentId}")
	      public ResponseEntity<String> updateAgent(
	              @PathVariable Long agentId,
	              @RequestBody AgentRequestDto agentRequestDto) {
	    	  
	    	  if (agentId == null) {
	    	        throw new AllExceptions.UserNotFoundException( "Agent ID must not be null");
	    	    }
	    	  logger.info("To updateAgent of if ",agentId);
	    	  logger.info("Updating agent with ID: " + agentId);

	          return new ResponseEntity<String>(adminService.updateAgent(agentId, agentRequestDto), HttpStatus.OK);
	      }
		
		
	    @PutMapping("/{employeeId}/profile") 
	    public ResponseEntity<Void> updateProfile(@PathVariable Long employeeId, @RequestBody EmployeeRequestDto employeeRequestDto) { 
	        employeeService.updateProfile(employeeId, employeeRequestDto); 
	        return ResponseEntity.noContent().build(); 
	    } 
	 
	    @PutMapping("/customers/{customerId}/verify") 
	    public ResponseEntity<Void> verifyCustomerDocuments(@PathVariable Long customerId) { 
	        employeeService.verifyCustomerDocuments(customerId); 
	        return ResponseEntity.noContent().build(); 
	    } 
	    @PutMapping("/customers/{customerId}") 
	    public ResponseEntity<Void> editCustomerDetails(@PathVariable Long customerId, @RequestBody CustomerRequestDto customerRequestDto) { 
	        employeeService.editCustomerDetails(customerId, customerRequestDto); 
	        return ResponseEntity.noContent().build(); 
	    } 
	 
	    @PutMapping("/agents/{agentId}") 
	    public ResponseEntity<Void> editAgentDetails(@PathVariable Long agentId, @RequestBody AgentRequestDto agentRequestDto) { 
	        employeeService.editAgentDetails(agentId, agentRequestDto); 
	        return ResponseEntity.noContent().build(); 
	    } 
	 
	    @GetMapping("/agentById/{agentId}")
	      public ResponseEntity<AgentResponseDto> viewAgentById(@PathVariable(name = "agentId") long agentId) {
	          return new ResponseEntity<AgentResponseDto>(employeeService.findAgentById(agentId), HttpStatus.OK);
	      }
	      

	    @DeleteMapping("/deactivate-agent/{id}")
	    public ResponseEntity<String> deactivateAgent(@PathVariable("id") Long id) {
	        try {
	        	employeeService.deactivateAgent(id);
	            return new ResponseEntity<>("Agent deactivated successfully", HttpStatus.OK);
	        } catch (IllegalArgumentException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Error deactivating agent: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @GetMapping("/agentsByActiveStatus")
	      public ResponseEntity<PagedResponse<AgentResponseDto>> getAgentsByActiveStatus(
	              @RequestParam(name = "active") boolean active,
	              @RequestParam(name = "page", defaultValue = "0") int page,
	              @RequestParam(name = "size", defaultValue = "5") int size,
	              @RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
	              @RequestParam(name = "direction", defaultValue = "asc") String direction) {
	          
	          PagedResponse<AgentResponseDto> agents = adminService.getAgentsByActiveStatus(active, page, size, sortBy, direction);
	          return new ResponseEntity<>(agents, HttpStatus.OK);
	      }

	    
	    @GetMapping("/getAllAgents")
		public ResponseEntity<PagedResponse<AgentResponseDto>> getAllAgents(
				@RequestParam(name = "page", defaultValue = "0") int page,
				@RequestParam(name = "size", defaultValue = "5") int size,
				@RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
				@RequestParam(name = "direction", defaultValue = "asc") String direction) {
	    	logger.info("To get all Agents");
			PagedResponse<AgentResponseDto> agents = employeeService.getAllAgents(page, size, sortBy, direction);
			return new ResponseEntity<PagedResponse<AgentResponseDto>>(agents, HttpStatus.ACCEPTED);
		}
	    
	    @Operation(summary = "TO  get All Customers")
	    @GetMapping("/get-all-customers")
	    public ResponseEntity<PagedResponse<CustomerResponseDto>> getAllCustomers(
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = "5") int size) {

	        logger.info("Received request to get all customers with page {} and size {}.", page, size);
	        PagedResponse<CustomerResponseDto> pagedResponse = employeeService.getAllCustomers(page, size);
	        logger.info("Retrieved {} customers on page {}.", pagedResponse.getContent().size(), page);
	        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
	    }
	    
//	    @GetMapping("/agents/{agentId}/commissions") 
//	    public ResponseEntity<List<CommissionReport>> viewCommissionReports(@PathVariable Long agentId) { 
//	        List<CommissionReport> reports = employeeService.viewCommissionReports(agentId); 
//	        return ResponseEntity.ok(reports); 
//	    } 
	 
//	    @PostMapping("/verifyCustomer/{customerId}")
//	    public ResponseEntity<String> verifyCustomer(@PathVariable Long customerId) {
//	        String response = employeeService.verifyCustomerById(customerId);
//	        return ResponseEntity.ok(response);
//	    }
	    @Operation(summary = "employee verification for customer")
	    @PutMapping("/verify-customer/{customerId}")
	    public ResponseEntity<String> verifyCustomer(@PathVariable Long customerId, @RequestBody VerificationDto verificationDto) {
	        PendingVerification pendingVerification = pendingVerificationRepository.findByCustomerId(customerId)
	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Verification request not found"));

	        if (!pendingVerification.getPanCard().equals(verificationDto.getPanCard())) {
	            throw new APIException(HttpStatus.BAD_REQUEST, "PAN Card number does not match");
	        }
	        if (!pendingVerification.getAadhaarCard().equals(verificationDto.getAadhaarCard())) {
	            throw new APIException(HttpStatus.BAD_REQUEST, "Aadhaar Card number does not match");
	        }

	        // Find and update the Customer entity
	        Customer customer = customerRepository.findById(customerId)
	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found"));

	        customer.setVerified(true);
	        customerRepository.save(customer);

	        // Mark the pending verification as complete
	        pendingVerification.setVerified(true);
	        pendingVerificationRepository.save(pendingVerification);

	        return ResponseEntity.ok("Customer verified successfully");
	    }
	    
	    @Operation(summary = "new verifivation")
	    @PutMapping("/verify/{customerId}")
	    public ResponseEntity<String> verifyCustomer(@PathVariable long customerId) {
	        try {
	            employeeService.verifyCustomer(customerId);
	            return ResponseEntity.ok("Customer verified successfully.");
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while verifying the customer.");
	        }
	    }
	    
	    @PutMapping("/{employeeId}/change-password") 
	    public ResponseEntity<String> changePassword(@PathVariable Long employeeId, @RequestBody ChangePasswordDto changePasswordDto) { 
	        employeeService.changePassword(employeeId, changePasswordDto); 
	        return ResponseEntity.ok("Password changed successfully"); 
	    }
	    
//	    @PostMapping("/{customerId}/verify-documents")
//	    public ResponseEntity<String> verifyDocuments(
//	            @PathVariable(name = "customerId") long customerId,
//	            @RequestBody DocumentVerificationDto documentVerificationDto) {
//	        
//	        try {
//	            boolean isVerified = employeeService.verifyDocuments(customerId, documentVerificationDto);
//	            if (isVerified) {
//	                return new ResponseEntity<>("Documents have been successfully verified.", HttpStatus.OK);
//	            } else {
//	                return new ResponseEntity<>("Document verification failed.", HttpStatus.BAD_REQUEST);
//	            }
//	        } catch (APIException e) {
//	            return new ResponseEntity<>(e.getMessage(), e.getStatus());
//	        }
//	    }
	    
	    @GetMapping("/get-customer-by-id/{id}")
	    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable("id") long customerId) {
	        CustomerResponseDto customerResponseDto = customerService.getCustomerById(customerId);
	        return new ResponseEntity<>(customerResponseDto, HttpStatus.OK);
	    }

	    @GetMapping("/search-by-name")
	    public ResponseEntity<PagedResponse<CustomerResponseDto>> searchCustomersByName(
	            @RequestParam("name") String name,
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = "10") int size) {

	        PagedResponse<CustomerResponseDto> pagedResponse = customerService.searchCustomersByName(name, page, size);
	        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
	    }

	    @GetMapping("/search-by-active-status")
	    public ResponseEntity<PagedResponse<CustomerResponseDto>> searchCustomersByActiveStatus(
	            @RequestParam("active") boolean active,
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = "10") int size) {

	        PagedResponse<CustomerResponseDto> pagedResponse = customerService.searchCustomersByActiveStatus(active, page, size);
	        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
	    }
	    
//	    @PostMapping("/{policyId}/verify-documents")
//	    public ResponseEntity<String> verifyPolicyDocuments(
//	            @PathVariable Long policyId,
//	            @RequestBody DocumentVerificationDto verificationDto) {
//	        
//	        // Fetch the insurance policy
//	        InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
//	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Policy not found with ID: " + policyId));
//
//	        // Fetch the customer associated with this policy
//	        Customer customer = policy.getCustomers().stream().findFirst()
//	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found for policy ID: " + policyId));
//
//	        // Fetch the insurance scheme related to the policy
//	        InsuranceScheme insuranceScheme = policy.getInsuranceScheme();
//	        
//	        // Retrieve required documents from the scheme
//	        Set<SchemeDocument> requiredDocuments = insuranceScheme.getSchemeDocuments();
//	        Set<String> requiredDocumentNames = requiredDocuments.stream()
//	                .map(SchemeDocument::getName)
//	                .collect(Collectors.toSet());
//
//	        // Fetch submitted documents from the policy
//	        Set<SubmittedDocument> submittedDocuments = policy.getDocuments();
//	        Set<String> submittedDocumentNames = submittedDocuments.stream()
//	                .map(SubmittedDocument::getDocumentName)
//	                .collect(Collectors.toSet());
//
//	        // Verify if all required documents are submitted
//	        if (!requiredDocumentNames.equals(submittedDocumentNames)) {
//	            policy.setPolicyStatus(PolicyStatus.REJECTED.name());
//	            insurancePolicyRepository.save(policy);
//	            return new ResponseEntity<>("Document verification failed: not all required documents are submitted.", HttpStatus.BAD_REQUEST);
//	        }
//
//	        // Check the verification status of each submitted document
//	        for (VerifiedDocumentDto verifiedDocument : verificationDto.getDocuments()) {
//	            String documentName = verifiedDocument.getDocumentName();
//	            boolean isVerified = verifiedDocument.isVerified();
//
//	            // Find the corresponding submitted document
//	            SubmittedDocument submittedDoc = submittedDocuments.stream()
//	                    .filter(doc -> doc.getDocumentName().equals(documentName))
//	                    .findFirst()
//	                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Unexpected document: " + documentName));
//
//	            // Update the status based on verification
//	            if (isVerified) {
//	                submittedDoc.setDocumentStatus(DocumentStatus.VERIFIED.name());
//	            } else {
//	                submittedDoc.setDocumentStatus(DocumentStatus.REJECTED.name());
//	            }
//
//	            submittedDocumentRepository.save(submittedDoc);
//	        }
//
//	        // If all documents are verified, set policy status to ACTIVE
//	        boolean allDocumentsVerified = submittedDocuments.stream()
//	                .allMatch(doc -> DocumentStatus.VERIFIED.name().equals(doc.getDocumentStatus()));
//
//	        if (allDocumentsVerified) {
//	            policy.setPolicyStatus(PolicyStatus.ACTIVE.name());
//	            insurancePolicyRepository.save(policy);
//	            return new ResponseEntity<>("Documents have been successfully verified. Policy is now active.", HttpStatus.OK);
//	        } else {
//	            policy.setPolicyStatus(PolicyStatus.REJECTED.name());
//	            insurancePolicyRepository.save(policy);
//	            return new ResponseEntity<>("Document verification failed. Policy has been rejected.", HttpStatus.BAD_REQUEST);
//	        }
//	}
	    @PostMapping("/{policyId}/verify-documents")
	    public ResponseEntity<String> verifyPolicyDocuments(
	            @PathVariable Long policyId,
	            @RequestBody DocumentVerificationDto verificationDto) {

	        // Fetch the insurance policy
	        InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Policy not found with ID: " + policyId));

	        // Fetch the customer associated with this policy
	        Customer customer = policy.getCustomers().stream().findFirst()
	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found for policy ID: " + policyId));

	        // Fetch the insurance scheme related to the policy
	        InsuranceScheme insuranceScheme = policy.getInsuranceScheme();
	        
	        // Retrieve required documents from the scheme
	        Set<SchemeDocument> requiredDocuments = insuranceScheme.getSchemeDocuments();
	        Set<String> requiredDocumentNames = requiredDocuments.stream()
	                .map(SchemeDocument::getName)
	                .map(String::toUpperCase) // Convert to uppercase for case-insensitive matching
	                .collect(Collectors.toSet());

	        // Fetch submitted documents from the policy
	        Set<SubmittedDocument> submittedDocuments = policy.getDocuments();

	        // Log both required and submitted documents
	        System.out.println("Required Documents for Insurance Scheme: " + insuranceScheme.getInsuranceScheme());
	        for (SchemeDocument doc : requiredDocuments) {
	            System.out.println("Required Document: " + doc.getName());
	        }

	        System.out.println("Submitted Documents for Policy ID: " + policyId);
	        for (SubmittedDocument doc : submittedDocuments) {
	            System.out.println("Submitted Document: " + doc.getDocumentName() + ", Status: " + doc.getDocumentStatus());
	        }

	        // Verify if all required documents are submitted
	        Set<String> submittedDocumentNames = submittedDocuments.stream()
	                .map(SubmittedDocument::getDocumentName)
	                .map(String::toUpperCase) // Convert to uppercase for case-insensitive matching
	                .collect(Collectors.toSet());

	        if (!requiredDocumentNames.equals(submittedDocumentNames)) {
	            policy.setPolicyStatus(PolicyStatus.REJECTED.name());
	            insurancePolicyRepository.save(policy);
	            return new ResponseEntity<>("Document verification failed: not all required documents are submitted.", HttpStatus.BAD_REQUEST);
	        }

	        // Check the verification status of each submitted document based on document name
	        for (VerifiedDocumentDto verifiedDocument : verificationDto.getDocuments()) {
	            String documentName = verifiedDocument.getDocumentName().toUpperCase(); // Case-insensitive match
	            boolean isVerified = verifiedDocument.isVerified();

	            // Find the corresponding submitted document by matching the document name
	            SubmittedDocument submittedDoc = submittedDocuments.stream()
	                    .filter(doc -> doc.getDocumentName().equalsIgnoreCase(documentName)) // Case-insensitive comparison
	                    .findFirst()
	                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Unexpected document: " + documentName));

	            // Update the status based on verification
	            if (isVerified) {
	                submittedDoc.setDocumentStatus(DocumentStatus.VERIFIED.name());
	            } else {
	                submittedDoc.setDocumentStatus(DocumentStatus.REJECTED.name());
	            }

	            // Save the updated status (ensure it's properly saved)
	            submittedDocumentRepository.save(submittedDoc);
	            System.out.println("Updated Document Status for " + documentName + ": " + submittedDoc.getDocumentStatus());
	        }

	        // Check if all documents are verified
	        boolean allDocumentsVerified = submittedDocuments.stream()
	                .allMatch(doc -> DocumentStatus.VERIFIED.name().equals(doc.getDocumentStatus()));

	        if (allDocumentsVerified) {
	            policy.setPolicyStatus(PolicyStatus.APPROVED.name()); // Mark policy as approved
	            policy.setVerified(true);  // Set verified flag to true
	            insurancePolicyRepository.save(policy);
	            return new ResponseEntity<>("Documents have been successfully verified. Policy is now approved.", HttpStatus.OK);
	        } else {
	            policy.setPolicyStatus(PolicyStatus.REJECTED.name());
	            policy.setVerified(false);  // Set verified flag to false if verification fails
	            insurancePolicyRepository.save(policy);
	            return new ResponseEntity<>("Document verification failed. Policy has been rejected.", HttpStatus.BAD_REQUEST);
	        }
	    }

	    @Operation(summary = "Verify Policy by Employee")
	    @PutMapping("/{id}/verify-policy")
	    public ResponseEntity<InsurancePolicyDto> verifyPolicy(@PathVariable(name = "id")Long policyId,
	                                      @RequestBody List<SubmittedDocumentDto> documentDtos){
	    	InsurancePolicyDto response = insuranceService.verifyPolicyDocuments(policyId, documentDtos);
	      return ResponseEntity.ok(response);
	    }
	    
	    @GetMapping("/policy/{policyId}")
	    public ResponseEntity<List<SubmittedDocument>> getDocumentsByPolicyId(@PathVariable Long policyId) {
	        List<SubmittedDocument> documents = submittedDocumentService.getDocumentsByPolicyId(policyId);
	        return ResponseEntity.ok(documents);
	    }
	    
	    @GetMapping("/policy/customer/{customerId}")
	    public ResponseEntity<PagedResponse<InsurancePolicyDto>> getPoliciesByCustomerId(
	            @PathVariable Long customerId,
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = "10") int size) {

	        PagedResponse<InsurancePolicyDto> policiesPage = insuranceService.getPoliciesByCustomerId(customerId, page, size);
	        
	        if (policiesPage.getContent().isEmpty()) {
	            return ResponseEntity.noContent().build();
	        } else {
	            return ResponseEntity.ok(policiesPage);
	        }
}
}

