package com.techlabs.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.repository.InsuranceSchemeRepository;

@Service
public class InsuranceSchemeServiceImpl implements InsuranceSchemeService{
	
	 @Autowired
	    private InsuranceSchemeRepository insuranceSchemeRepository;


	@Override
	public InsuranceScheme findById(Long id) {
		
		return insuranceSchemeRepository.findById(id)
	            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	                "Insurance scheme not found with ID: " + id));
	}

}
