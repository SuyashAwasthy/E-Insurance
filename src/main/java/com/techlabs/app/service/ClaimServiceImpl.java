package com.techlabs.app.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.repository.ClaimRepository;

@Service
public class ClaimServiceImpl implements ClaimService{
	
	@Autowired 
	private ClaimRepository claimRepository;
	
	

}
