package com.techlabs.app.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.CityResponse;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.StateResponse;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.PaymentTax;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.service.AgentService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/E-Insurance/toall")
@CrossOrigin(origins="http://localhost:3000")
public class AllCOntroller {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private AdminService adminService;
	
	private AgentService agentService;

 
	
public AllCOntroller(AdminService adminService, AgentService agentService) {
		super();
		this.adminService = adminService;
		this.agentService = agentService;
	}

//	@Operation(summary = "Get all Insurance Plans")
//    @GetMapping("/getAllPlans")
//    public ResponseEntity<List<InsurancePlanDTO>> getAllInsurancePlans() {
//    	logger.info("To get All Plans");
//        List<InsurancePlanDTO> plans = adminService.getAllInsurancePlans();
//        return ResponseEntity.ok(plans);
//    }
	@Operation(summary = "Get all Insurance Plans")
  @GetMapping("/getAllPlans")
    public ResponseEntity<PagedResponse<InsurancePlanDTO>> getAllInsurancePlans(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "insurancePlanId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        logger.info("To get All Plans with pagination");
        PagedResponse<InsurancePlanDTO> pagedResponse = adminService.getAllPlans(page, size, sortBy, direction);
        return ResponseEntity.ok(pagedResponse);
    }
   
	 @Operation(summary = "Get all Insurance Schemes")
	    @GetMapping("/getAllSchemes")
//	    public ResponseEntity<List<InsuranceSchemeDto>> getAllInsuranceSchemes() {
//	    	logger.info("To get All Schemes");
//	        List<InsuranceSchemeDto> schemes = adminService.getAllInsuranceSchemes();
//	        return ResponseEntity.ok(schemes);
//	    }public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemes(
	 public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemes(
		      @RequestParam(name = "page", defaultValue = "0") int page,
		      @RequestParam(name = "size", defaultValue = "5") int size,
		      @RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
		      @RequestParam(name = "direction", defaultValue = "asc") String direction) {
		    PagedResponse<InsuranceSchemeDto> schemes = adminService.getAllSchemes(page, size, sortBy, direction);
		    return new ResponseEntity<>(schemes, HttpStatus.OK);
		  }

	 @GetMapping("/getSchemesByPlan/{planId}")
//	    public ResponseEntity<List<InsuranceSchemeDto>> getSchemesByPlan(@PathVariable Long planId) {
//	        List<InsuranceSchemeDto> schemes = adminService.getSchemesByPlan(planId);
//	        return ResponseEntity.ok(schemes);
//	    }
	 public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemesByPlanId(@PathVariable Long planId,
		      @RequestParam(name = "page", defaultValue = "0") int page,
		      @RequestParam(name = "size", defaultValue = "5") int size,
		      @RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
		      @RequestParam(name = "direction", defaultValue = "asc") String direction) {

		    PagedResponse<InsuranceSchemeDto> schemes = adminService.getAllSchemesByPlanId(planId, page, size, sortBy,
		        direction);
		    return new ResponseEntity<>(schemes, HttpStatus.OK);
		  }
	 @GetMapping("/viewAllstates")
	    public ResponseEntity<PagedResponse<StateResponse>> getAllStates(
	    	      @RequestParam(name = "page", defaultValue = "0") int page,
	    	      @RequestParam(name = "size", defaultValue = "5") int size,
	    	      @RequestParam(name = "sortBy", defaultValue = "stateId") String sortBy,
	    	      @RequestParam(name = "direction", defaultValue = "asc") String direction)  {
	    	logger.info("To get all states");
	    	    return new ResponseEntity<PagedResponse<StateResponse>>(
	    	        adminService.getAllStates(page, size, sortBy, direction), HttpStatus.OK);

	    	  }
	 
	 @GetMapping("/cities")
	    public ResponseEntity<PagedResponse<CityResponse>> getAllCities(
	      @RequestParam(name = "page", defaultValue = "0") int page,
	      @RequestParam(name = "size", defaultValue = "50") int size,
	      @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
	      @RequestParam(name = "direction", defaultValue = "asc") String direction){
	    	System.out.println("To get all cities");
	      return new ResponseEntity<PagedResponse<CityResponse>>(adminService.getAllCities(page, size, sortBy, direction),HttpStatus.OK);
	      
	      
	    }
	 

		@Operation(summary = "TO  get All Customers")
	    @GetMapping("/get-all-customers")
	    public ResponseEntity<PagedResponse<CustomerResponseDto>> getAllCustomers(
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = "5") int size) {

	        logger.info("Received request to get all customers with page {} and size {}.", page, size);
	        PagedResponse<CustomerResponseDto> pagedResponse = agentService.getAllCustomers(page, size);
	        logger.info("Retrieved {} customers on page {}.", pagedResponse.getContent().size(), page);
	        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
	    }
		
		 @GetMapping("/payment-tax")
	      public ResponseEntity<PaymentTax> getPaymentTax() {
	          PaymentTax paymentTax = adminService.getPaymentTax();
	          return ResponseEntity.ok(paymentTax);
	      }

		   @Operation(summary = "Get all DocumentTypes")
		    @GetMapping("/getall/document-types")
		    public ResponseEntity<List<String>> getDocumentTypes() {
		        List<String> documentTypes = Arrays.stream(DocumentType.values())
		                .map(Enum::name)
		                .collect(Collectors.toList());
		        return ResponseEntity.ok(documentTypes);
		    }

	    
}
