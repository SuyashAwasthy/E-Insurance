package com.techlabs.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.StripeChargeDto;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.service.ClaimService;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.service.InsurancePolicyService;
import com.techlabs.app.service.InsuranceSchemeService;
import com.techlabs.app.service.StripeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/E-Insurance/customer")
@PreAuthorize("hasRole('CUSTOMER')")

public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InsurancePolicyService insurancePolicyService;

	@Autowired
	private InsuranceSchemeService insuranceSchemeRepository;

	@Autowired
	private ClaimService claimService;
	
	@Autowired
	StripeService stripService;

	public CustomerController(CustomerService customerService, InsurancePolicyService insurancePolicyService,
			InsuranceSchemeService insuranceSchemeRepository) {
		super();
		this.customerService = customerService;
		this.insurancePolicyService = insurancePolicyService;
		this.insuranceSchemeRepository = insuranceSchemeRepository;
	}

	@PostMapping("/registerPolicyForCustomer")
	public ResponseEntity<String> registerPolicyForCustomer(@RequestParam long customerId, @RequestParam long policyId,
			@RequestParam long agentId) {
		try {
			String response = insurancePolicyService.registerPolicyForCustomer(customerId, policyId, agentId);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/add")
	public ResponseEntity<String> addCustomer(@RequestBody RegisterDto registerDto) {
		try {
			customerService.addCustomer(registerDto);
			return new ResponseEntity<>("Customer registered successfully!", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error registering customer: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get-all-customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}
//	    
//	    @Operation(summary = "Create Insurance Policy")
//		@PostMapping("/createPolicy")
//		public ResponseEntity<String> createInsurancePolicy(@RequestBody InsurancePolicyDto insurancePolicyDto) {
//			logger.info("Creating insurance policy: {}", insurancePolicyDto);
//			String response = customerService.createInsurancePolicy(insurancePolicyDto);
//			return new ResponseEntity<>(response, HttpStatus.CREATED);
//		}

//	    @PostMapping("/customers/{customerId}/buy-policy")
//	    public ResponseEntity<String> buyPolicy(@RequestBody PolicyAccountRequestDto accountRequestDto,@PathVariable(name = "customerId") long customerId){
//	    	System.out.println("iuytre");
//	      return new ResponseEntity<String>(customerService.buyPolicy(accountRequestDto,customerId),HttpStatus.OK);
//	      
//	    }

	@PostMapping("/{customerId}/buy-policy")
	public ResponseEntity<String> buyPolicy(@RequestBody InsurancePolicyDto accountRequestDto,
			@PathVariable(name = "customerId") long customerId) {
		return new ResponseEntity<String>(customerService.buyPolicy(accountRequestDto, customerId), HttpStatus.OK);

	}

	@PostMapping("/buyWithoutAgent")
	public ResponseEntity<String> buyPolicyWithoutAgent(@RequestBody @Valid InsurancePolicyDto accountRequestDto,
			@RequestParam long customerId) {
		InsuranceScheme insuranceScheme = insurancePolicyService
				.getInsuranceScheme(accountRequestDto.getInsuranceSchemeId());

		String response = customerService.buyPolicyWithoutAgent(accountRequestDto, customerId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/claim")
	public ResponseEntity<String> claimPolicy(@RequestBody ClaimRequestDto claimRequestDto,@RequestParam Long customerId){
		String response =customerService.claimPolicy(claimRequestDto,customerId);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/cancel")
	 public ResponseEntity<String> cancelPolicy(@RequestBody CancellationRequestDto cancellationRequest, @RequestParam Long customerId) {
        String response = customerService.requestPolicyCancellation(cancellationRequest, customerId);
        return ResponseEntity.ok(response);
    }
	@PostMapping("/test")
	public String testing(@RequestBody StripeChargeDto stripeCharge) {
		stripService.charge(stripeCharge);
		return "success";

	}
	
	@PostMapping("/cancelPolicy")
    public ResponseEntity<String> customerCancelPolicy(@RequestBody ClaimRequestDto claimRequestDto,
                                                       @RequestParam Long customerId) {
        String response = customerService.customerCancelPolicy(claimRequestDto, customerId);
        return ResponseEntity.ok(response);
    }
	
	
//	    @GetMapping("/customer/{customerId}")
//	    public ResponseEntity<List<InsurancePolicy>> getPoliciesByCustomer(
//	            @PathVariable Long customerId) {
//
//	        List<InsurancePolicy> policies = insurancePolicyService.getPoliciesByCustomerId(customerId);
//	        return ResponseEntity.ok(policies);
//	    }
//	   
}
