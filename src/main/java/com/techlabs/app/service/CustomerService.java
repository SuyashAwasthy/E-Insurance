package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsuranceScheme;

public interface CustomerService {

	void addCustomer(RegisterDto registerDto);

	List<Customer> getAllCustomers();

	String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto);

	String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId);

	String buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId);

	String claimPolicy(ClaimRequestDto claimRequestDto, long customerId);

	String requestPolicyCancellation(CancellationRequestDto cancellationRequest, Long customerId);

	String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId);

	
	//String buyPolicy(PolicyAccountRequestDto accountRequestDto, long customerId);

}
