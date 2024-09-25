package com.techlabs.app.service;

import java.util.List;
import java.util.Optional;

import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.util.PagedResponse;

public interface InsurancePolicyService  {

	String registerPolicyForCustomer(long customerId, long policyId, long agentId);

	InsuranceScheme getInsuranceScheme(Long insuranceSchemeId);

	String createPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId);

	InsurancePolicyDto verifyPolicyDocuments(Long policyId, List<SubmittedDocumentDto> documentDtos);

	InsurancePolicyDto updateSubmittedDocuments(Long policyId, List<SubmittedDocumentDto> documentDtos);

	List<InsurancePolicy> getPoliciesByAgentId(Long agentId);

	int countTotalInsurancePolicies();

	List<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId);

	PagedResponse<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId, int page, int size);

	

	//List<InsurancePolicy> getPoliciesByCustomerId(Long customerId);

}
