package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.InsurancePolicy;

public interface CommissionService {
	 List<Commission> getCommissionsForAgent(String username);
	    void addCommissionForAgent(String username, Commission commission);
	    double calculateCommission(InsurancePolicy policy, Agent agent);


}
