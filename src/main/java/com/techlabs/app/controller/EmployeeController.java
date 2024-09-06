package com.techlabs.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.VerificationDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.PendingVerification;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.PendingVerificationRepository;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/E-Insurance/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {
	 @Autowired
	    private EmployeeService employeeService;
	 
	 @Autowired
	    private CustomerRepository customerRepository;

	    @Autowired
	    private PendingVerificationRepository pendingVerificationRepository;
	 
	

	    @PostMapping("/registerAgent")
	    public ResponseEntity<String> registerAgent(@RequestBody @Valid AgentRequestDto agentRequestDto) {
	      System.out.println(agentRequestDto);
	        String response = employeeService.registerAgent(agentRequestDto);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
	    
	}
