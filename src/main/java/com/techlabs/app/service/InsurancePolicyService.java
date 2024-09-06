package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;

public interface InsurancePolicyService  {

	String registerPolicyForCustomer(long customerId, long policyId, long agentId);

	InsuranceScheme getInsuranceScheme(Long insuranceSchemeId);

	String createPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId);

	

	//List<InsurancePolicy> getPoliciesByCustomerId(Long customerId);

}
