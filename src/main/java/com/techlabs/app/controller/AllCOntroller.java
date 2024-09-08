package com.techlabs.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/E-Insurance/toall")
@CrossOrigin(origins="http://localhost:3000")
public class AllCOntroller {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private AdminService adminService;

    public AllCOntroller(AdminService adminService) {
        this.adminService = adminService;
    }
	
	@Operation(summary = "Get all Insurance Plans")
    @GetMapping("/getAllPlans")
    public ResponseEntity<List<InsurancePlanDTO>> getAllInsurancePlans() {
    	logger.info("To get All Plans");
        List<InsurancePlanDTO> plans = adminService.getAllInsurancePlans();
        return ResponseEntity.ok(plans);
    }
   
	 @Operation(summary = "Get all Insurance Schemes")
	    @GetMapping("/getAllSchemes")
	    public ResponseEntity<List<InsuranceSchemeDto>> getAllInsuranceSchemes() {
	    	logger.info("To get All Schemes");
	        List<InsuranceSchemeDto> schemes = adminService.getAllInsuranceSchemes();
	        return ResponseEntity.ok(schemes);
	    }

	 @GetMapping("/getSchemesByPlan/{planId}")
	    public ResponseEntity<List<InsuranceSchemeDto>> getSchemesByPlan(@PathVariable Long planId) {
	        List<InsuranceSchemeDto> schemes = adminService.getSchemesByPlan(planId);
	        return ResponseEntity.ok(schemes);
	    }
}
