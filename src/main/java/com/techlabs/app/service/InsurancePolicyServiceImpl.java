package com.techlabs.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService{

	
	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;
	private CustomerRepository customerRepository;
	@Override
	public String registerPolicyForCustomer(long customerId, long policyId, long agentId) {
//		    Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
//		        InsurancePolicy policy = insurancePolicyRepository.findById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));     
//		        Agent agent = agentService.findAgentById(agentId);
//		        if (!customer.getCity().equals(agent.getCity())) {
//		        	
//		            throw new RuntimeException("Agent and customer must be in the same city.");        }
//		        policy.setAgent(agent); // Assign agent to the policy
//		        policy.getCustomers().add(customer); // Link customer to the policy        insurancePolicyRepository.save(policy);
		        return "Policy registered successfully.";
//		    }
//		}
	}
	@Override
	public InsuranceScheme getInsuranceScheme(Long insuranceSchemeId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String createPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public List<InsurancePolicy> getPoliciesByCustomerId(Long customerId) {
//		// TODO Auto-generated method stub
//		return insurancePolicyRepository.findByCustomersId(customerId);
//	}

}
