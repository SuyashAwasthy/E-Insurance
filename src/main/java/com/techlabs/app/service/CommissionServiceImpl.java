package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CommissionRepository;

@Service
public class CommissionServiceImpl implements CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private AgentRepository agentRepository;

	@Override
	public List<Commission> getCommissionsForAgent(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCommissionForAgent(String username, Commission commission) {
		// TODO Auto-generated method stub
		
	}
	
    private static final double COMMISSION_PERCENTAGE = 0.05; // 5% commission

    @Override
    public double calculateCommission(InsurancePolicy policy, Agent agent) {
        double commission = 0;

        // Calculate commission based on the premium amount
        if (policy != null && agent != null && policy.getPremiumAmount() > 0) {
            commission = policy.getPremiumAmount() * COMMISSION_PERCENTAGE;
            policy.setClaimAmount(commission);
        }

        return commission;
    }
    
    public List<Commission> getCommissionsInRange(LocalDateTime startDate, LocalDateTime endDate) {
        return commissionRepository.findByDateBetween(startDate, endDate);
    }

//    @Override
//    public List<Commission> getCommissionsForAgent(String username) {
//        // Find the agent by username
//        Agent agent = agentRepository.findByUsername(username);
//        if (agent == null) {
//            throw new RuntimeException("Agent not found");
//        }
//
//        // Retrieve and return the list of commissions for the agent
//        return commissionRepository.findByAgentId(agent.getAgentId());
//    }
//
//    @Override
//    public void addCommissionForAgent(String username, Commission commission) {
//        // Find the agent by username
//        Agent agent = agentRepository.findByUsername(username);
//        if (agent == null) {
//            throw new RuntimeException("Agent not found");
//        }
//
//        // Set the agent on the commission and save it
//        commission.setAgent(agent);
//        commissionRepository.save(commission);
//    }
}