package com.techlabs.app.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CityRequest;
import com.techlabs.app.dto.CityResponse;
import com.techlabs.app.dto.ClaimDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.StateRequest;
import com.techlabs.app.dto.StateResponse;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.dto.WithdrawalRequestDto;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.PaymentTax;
import com.techlabs.app.entity.WithdrawalRequest;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.service.AgentService;
import com.techlabs.app.service.ClaimService;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.service.DocumentTypeService;
import com.techlabs.app.service.EmployeeService;
import com.techlabs.app.service.WithdrawalRequestService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/E-Insurance/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins="http://localhost:3000")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private AdminService adminService;
    private EmployeeService employeeService;
private CustomerService customerService;
private ClaimService claimService;
private AgentService agentService;
private DocumentTypeService documentTypeService;
	private WithdrawalRequestService withdrawalRequestService; 
	

	

	public AdminController(AdminService adminService, EmployeeService employeeService, CustomerService customerService,
			ClaimService claimService, AgentService agentService, DocumentTypeService documentTypeService,
			WithdrawalRequestService withdrawalRequestService) {
		super();
		this.adminService = adminService;
		this.employeeService = employeeService;
		this.customerService = customerService;
		this.claimService = claimService;
		this.agentService = agentService;
		this.documentTypeService = documentTypeService;
		this.withdrawalRequestService = withdrawalRequestService;
	}

	@Operation(summary = "Add Employee")
	@PostMapping("addEmployee")
	public ResponseEntity<String> registerEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
    	logger.info("Request to register employee: {}", employeeRequestDto);
		return new ResponseEntity<String>(adminService.registerEmployee(employeeRequestDto),
				HttpStatus.ACCEPTED);
	}
    
    @Operation(summary = "Add Agent")
	@PostMapping("addAgent")
	public ResponseEntity<String> registerAgent(@RequestBody AgentRequestDto agentRequestDto) {
		return new ResponseEntity<String>(adminService.registerAgent(agentRequestDto),
				HttpStatus.ACCEPTED);
	}
    
//    @GetMapping("/agentByName")
//    public ResponseEntity<AgentResponseDto> getAgentByName(@RequestParam String firstName, @RequestParam String lastName) {
//        return new ResponseEntity<>(agentService.getAgentByName(firstName, lastName), HttpStatus.OK);
//    }

	// Get Agent by Active Status
	@GetMapping("/agentsByActiveStatus/{active}")
	public ResponseEntity<List<AgentResponseDto>> getAgentsByActiveStatus(@PathVariable(name = "active") boolean active) {
	    return new ResponseEntity<List<AgentResponseDto>>(agentService.getAgentsByActiveStatus(active), HttpStatus.OK);
	}
    
    @Operation(summary = "view Customer By ID")
    @GetMapping("/view-customer-by-id/{customerId}")
    public ResponseEntity<CustomerResponseDto> viewCustomerbyId(@PathVariable(name = "customerId") long customerId) {
    return new ResponseEntity<CustomerResponseDto>(adminService.findCustomerByid(customerId), HttpStatus.OK);
  }
    @GetMapping("/getAllEmployees")
    public ResponseEntity<PagedResponse<EmployeeResponseDto>> getAllEmployees(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "sortBy", defaultValue = "employeeId") String sortBy,
        @RequestParam(name = "direction", defaultValue = "asc") String direction) {
      PagedResponse<EmployeeResponseDto> employees = adminService.getAllEmployees(page, size, sortBy, direction);
      return new ResponseEntity<PagedResponse<EmployeeResponseDto>>(employees, HttpStatus.ACCEPTED);
    }

      
      
      
    @Operation(summary = "view Employee By ID")
      @GetMapping("/view-employee-by-id/{employeeId}")
      public ResponseEntity<EmployeeResponseDto> viewEmployeebyId(@PathVariable(name = "employeeId") long employeeId) {
      return new ResponseEntity<EmployeeResponseDto>(adminService.findEmployeeByid(employeeId), HttpStatus.OK);
    }
    
    @GetMapping("/employeesByActiveStatus")
    public ResponseEntity<PagedResponse<EmployeeResponseDto>> getEmployeesByActiveStatus(
            @RequestParam(name = "active") boolean active,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "employeeId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        PagedResponse<EmployeeResponseDto> employees = adminService.getEmployeesByActiveStatus(active, page, size, sortBy, direction);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


      
      @GetMapping("/agentById/{agentId}")
      public ResponseEntity<AgentResponseDto> viewAgentById(@PathVariable(name = "agentId") long agentId) {
          return new ResponseEntity<AgentResponseDto>(adminService.findAgentById(agentId), HttpStatus.OK);
      }
      
      @GetMapping("/agentById")
      public ResponseEntity<PagedResponse<AgentResponseDto>> getAgentById(
              @RequestParam(name = "agentId") Long agentId,
              @RequestParam(name = "page", defaultValue = "0") int page,
              @RequestParam(name = "size", defaultValue = "5") int size,
              @RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
              @RequestParam(name = "direction", defaultValue = "asc") String direction) {
          
          AgentResponseDto agent = adminService.getAgentById(agentId);

          // Since we are fetching by ID, the total elements will be 1 and total pages will be 1
          PagedResponse<AgentResponseDto> pagedResponse = new PagedResponse<>(
                  List.of(agent), page, size, 1, 1, true
          );

          return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
      }
      @GetMapping("/agentsByActiveStatus")
      public ResponseEntity<PagedResponse<AgentResponseDto>> getAgentsByActiveStatus(
              @RequestParam(name = "active") boolean active,
              @RequestParam(name = "page", defaultValue = "0") int page,
              @RequestParam(name = "size", defaultValue = "5") int size,
              @RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
              @RequestParam(name = "direction", defaultValue = "asc") String direction) {
          
          PagedResponse<AgentResponseDto> agents = adminService.getAgentsByActiveStatus(active, page, size, sortBy, direction);
          return new ResponseEntity<>(agents, HttpStatus.OK);
      }

      @Operation(summary = "Update Agent")
      @PutMapping("updateAgent/{agentId}")
      public ResponseEntity<String> updateAgent(
              @PathVariable Long agentId,
              @RequestBody AgentRequestDto agentRequestDto) {
    	  logger.info("To updateAgent of if ",agentId);
          return new ResponseEntity<String>(adminService.updateAgent(agentId, agentRequestDto), HttpStatus.OK);
      }

      @Operation(summary = "Update Employee")
      @PutMapping("updateEmployee/{employeeId}")
      public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeRequestDto employeeRequestDto) {
    	  logger.info("To updateEmployee of if ",employeeId);
          return new ResponseEntity<>(adminService.updateEmployee(employeeId, employeeRequestDto), HttpStatus.OK);
      }
    @GetMapping("/getAllAgents")
	public ResponseEntity<PagedResponse<AgentResponseDto>> getAllAgents(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
    	logger.info("To get all Agents");
		PagedResponse<AgentResponseDto> agents = adminService.getAllAgents(page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<AgentResponseDto>>(agents, HttpStatus.ACCEPTED);
	}

    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateEmployee(@PathVariable("id") Long id) {
        try {
            employeeService.deactivateEmployee(id);
            return new ResponseEntity<>("Employee deactivated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deactivating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/deactivate-customer/{id}")
    public ResponseEntity<String> deactivateCustomer(@PathVariable("id") Long id) {
        try {
           adminService.deactivateCustomer(id);
            return new ResponseEntity<>("customer deactivated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deactivating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/deactivate-agent/{id}")
    public ResponseEntity<String> deactivateAgent(@PathVariable("id") Long id) {
        try {
            adminService.deactivateAgent(id);
            return new ResponseEntity<>("Agent deactivated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deactivating agent: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/tax-setting")
    public ResponseEntity<String> createTaxSetting(@RequestBody TaxSettingRequestDto taxSettingRequestDto) {
      return new ResponseEntity<String>(adminService.createTaxSetting(taxSettingRequestDto),HttpStatus.CREATED);
      
    }

    
    
    @PostMapping("/create-state")
    public ResponseEntity<String> createState(@RequestBody StateRequest stateRequest){
      String response=adminService.createState(stateRequest);
      return new ResponseEntity<>(response,HttpStatus.CREATED);
      
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
    	    
    	    @DeleteMapping("/deactivate-state/{id}")
    	    public ResponseEntity<String>deactivateState(@PathVariable(name="id") long id){
    	    	logger.info("To DeActivateState of id ",id);
    	      return new ResponseEntity<String>(adminService.deactivateStateById(id),HttpStatus.OK);
    	    }
    	    
    	    @PutMapping("/state/{id}")
    	    public ResponseEntity<String>activateState(@PathVariable(name="id") long id){
    	    	logger.info("To ActivateState of id ",id);
    	      return new ResponseEntity<String>(adminService.activateStateById(id),HttpStatus.OK);
    	    }
    	    
    	    @PostMapping("/create-city")
    	    public ResponseEntity<String> createCity(@RequestBody CityRequest cityRequest){
    	    	logger.info("Creating the city");
    	      
    	    //  String response = adminService.createCity(cityRequest);
    	     // return new ResponseEntity<>(response,HttpStatus.CREATED);
    	    	try {
    	            String response = adminService.createCity(cityRequest);
    	            return new ResponseEntity<>(response, HttpStatus.CREATED);
    	        } catch (Exception e) {
    	            // Log and handle the exception appropriately
    	            return new ResponseEntity<>("Error creating city: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    	        }
    	      
    	    }
    	    
    	    @DeleteMapping("/city/{id}")
    	    public ResponseEntity<String>deactivateCity(@PathVariable(name="id") long id){
    	    	logger.info("To de-activate the city of id ",id);
    	      return new ResponseEntity<String>(adminService.deactivateCity(id),HttpStatus.OK);
    	      
    	    }
    	    
    	    @GetMapping("/city/{id}")
    	    public ResponseEntity<CityResponse>getCityById(@PathVariable(name="id") long id){
    	    	logger.info("To get  the city of id ",id);
    	      return new ResponseEntity<CityResponse>(adminService.getCityById(id),HttpStatus.OK);
    	    }
    	    
    	    @Operation(summary = "Activate city ")
    	    @PutMapping("/city/{id}")
    	    public ResponseEntity<String>activateCity(@PathVariable(name="id") long id){
    	    	logger.info("Activating ths city of id ",id);
    	      return new ResponseEntity<String>(adminService.activateCity(id),HttpStatus.OK);
    	    }
    	    
    	    @GetMapping("/cities")
    	    public ResponseEntity<PagedResponse<CityResponse>> getAllCities(
    	      @RequestParam(name = "page", defaultValue = "0") int page,
    	      @RequestParam(name = "size", defaultValue = "5") int size,
    	      @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
    	      @RequestParam(name = "direction", defaultValue = "asc") String direction){
    	    	System.out.println("To get all cities");
    	      return new ResponseEntity<PagedResponse<CityResponse>>(adminService.getAllCities(page, size, sortBy, direction),HttpStatus.OK);
    	      
    	      
    	    }
    	    
    	    @Operation(summary = "TO  get All Customers")
    	    @GetMapping("/get-all-customers")
    	    public ResponseEntity<PagedResponse<CustomerResponseDto>> getAllCustomers(
    	            @RequestParam(value = "page", defaultValue = "0") int page,
    	            @RequestParam(value = "size", defaultValue = "10") int size) {

    	        logger.info("Received request to get all customers with page {} and size {}.", page, size);
    	        PagedResponse<CustomerResponseDto> pagedResponse = adminService.getAllCustomers(page, size);
    	        logger.info("Retrieved {} customers on page {}.", pagedResponse.getContent().size(), page);
    	        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    	    }
    	    

    	    @Operation(summary = "Create Insurance Plan")
    		@PostMapping("/createPlan")
    		public ResponseEntity<String> createInsurancePlan(@RequestBody InsurancePlanDTO insurancePlanDto) {
    			logger.info("Creating insurance plan: {}", insurancePlanDto);
    			String response = adminService.createInsurancePlan(insurancePlanDto);
    			return new ResponseEntity<>(response, HttpStatus.CREATED);
    		}

    	   
    	    
//    		@Operation(summary = "Create Insurance Scheme")
//    		@PostMapping("/createScheme")
//    		public ResponseEntity<String> createInsuranceScheme(@RequestBody InsuranceSchemeDto insuranceSchemeDto) {
//    			logger.info("Creating insurance scheme: {}", insuranceSchemeDto);
//    			String response = adminService.createInsuranceScheme(insuranceSchemeDto);
//    			return new ResponseEntity<>(response, HttpStatus.CREATED);
//    		}

//    	    @Operation(summary = "Create Insurance Scheme")
//    		@PostMapping("/createScheme")
//    	    public ResponseEntity<String> createInsuranceScheme(@RequestParam("insurancePlanId") long insurancePlanId,
//
//    	    	      @Valid @RequestBody InsuranceSchemeDto insuranceSchemeDto) {
//
//    	    	    String response = adminService.createInsuranceScheme(insurancePlanId, insuranceSchemeDto);
//
//    	    	    return new ResponseEntity<>(response, HttpStatus.CREATED);
//    	    	  }
//    	    from simran
    	    @Operation(summary = "Create Insurance Scheme") 
    	      @PostMapping("/createScheme/{insurancePlanId}") 
    	         public ResponseEntity<String> createInsuranceScheme(@PathVariable("insurancePlanId") long insurancePlanId, 
    	 
    	               @RequestBody InsuranceSchemeDto insuranceSchemeDto) { 
    	 
    	              String response = adminService.createInsuranceScheme(insurancePlanId, insuranceSchemeDto); 
    	 
    	              return new ResponseEntity<>(response, HttpStatus.CREATED); 
    	            }
    	    
//    		@Operation(summary = "Create Insurance Policy")
//    		@PostMapping("/createPolicy")
//    		public ResponseEntity<String> createInsurancePolicy(@RequestBody InsurancePolicyDto insurancePolicyDto) {
//    			logger.info("Creating insurance policy: {}", insurancePolicyDto);
//    			String response = adminService.createInsurancePolicy(insurancePolicyDto);
//    			return new ResponseEntity<>(response, HttpStatus.CREATED);
//    		}
    		
    		@PutMapping("updateInsurancePlan/{planId}")
    	    @Operation(summary = "Update Insurance Plan")
    	    public ResponseEntity<String> updateInsurancePlan(@PathVariable Long planId, @RequestBody InsurancePlanDTO insurancePlanDto) {
    	        logger.info("Updating insurance plan with ID {}: {}", planId, insurancePlanDto);
    	        try {
    	            String response = adminService.updateInsurancePlan(planId, insurancePlanDto);
    	            logger.info("Insurance plan updated successfully.");
    	            return new ResponseEntity<>(response, HttpStatus.OK);
    	        } catch (Exception e) {
    	            logger.error("Error updating insurance plan: {}", e.getMessage());
    	            throw e;
    	        }
    	    }

    	    @PutMapping("updateInsuranceScheme/{schemeId}")
    	    @Operation(summary = "Update Insurance Scheme")
    	    public ResponseEntity<String> updateInsuranceScheme(@PathVariable Long schemeId, @RequestBody InsuranceSchemeDto insuranceSchemeDto) {
    	        logger.info("Updating insurance scheme with ID {}: {}", schemeId, insuranceSchemeDto);
    	        try {
    	            String response = adminService.updateInsuranceScheme(schemeId, insuranceSchemeDto);
    	            logger.info("Insurance scheme updated successfully.");
    	            return new ResponseEntity<>(response, HttpStatus.OK);
    	        } catch (Exception e) {
    	            logger.error("Error updating insurance scheme: {}", e.getMessage());
    	            throw e;
    	        }
    	    }

    	    @PutMapping("updateInsurancePolicy/{policyId}")
    	    @Operation(summary = "Update Insurance Policy")
    	    public ResponseEntity<String> updateInsurancePolicy(@PathVariable Long policyId, @RequestBody InsurancePolicyDto insurancePolicyDto) {
    	        logger.info("Updating insurance policy with ID {}: {}", policyId, insurancePolicyDto);
    	        try {
    	            String response = adminService.updateInsurancePolicy(policyId, insurancePolicyDto);
    	            logger.info("Insurance policy updated successfully.");
    	            return new ResponseEntity<>(response, HttpStatus.OK);
    	        } catch (Exception e) {
    	            logger.error("Error updating insurance policy: {}", e.getMessage());
    	            throw e;
    	        }
    	    }
    		
    	    @Operation(summary = "Get all Insurance Plans")
    	    @GetMapping("/getAllPlans")
//    	    public ResponseEntity<List<InsurancePlanDTO>> getAllInsurancePlans() {
//    	    	logger.info("To get All Plans");
//    	        List<InsurancePlanDTO> plans = adminService.getAllInsurancePlans();
//    	        return ResponseEntity.ok(plans);
//    	    }
    	    public ResponseEntity<PagedResponse<InsurancePlanDTO>> getAllInsurancePlans(
    	            @RequestParam(name = "page", defaultValue = "0") int page,
    	            @RequestParam(name = "size", defaultValue = "25") int size,
    	            @RequestParam(name = "sortBy", defaultValue = "insurancePlanId") String sortBy,
    	            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
    	        logger.info("To get All Plans with pagination");
    	        PagedResponse<InsurancePlanDTO> pagedResponse = adminService.getAllPlans(page, size, sortBy, direction);
    	        return ResponseEntity.ok(pagedResponse);
    	    }
   	   
    	    @Operation(summary = "Get all Insurance Schemes")
    	    @GetMapping("/getAllSchemes")
    	    public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemes(
    	    	      @RequestParam(name = "page", defaultValue = "0") int page,
    	    	      @RequestParam(name = "size", defaultValue = "25") int size,
    	    	      @RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
    	    	      @RequestParam(name = "direction", defaultValue = "asc") String direction) {
    	    	    PagedResponse<InsuranceSchemeDto> schemes = adminService.getAllSchemes(page, size, sortBy, direction);
    	    	    return new ResponseEntity<>(schemes, HttpStatus.OK);
    	    	  }

    	    @Operation(summary = "Get all Insurance Policies")
    	    @GetMapping("/getAllPolicies")
    	    public ResponseEntity<List<InsurancePolicyDto>> getAllInsurancePolicies() {
    	    	logger.info("To get All Policies");
    	        List<InsurancePolicyDto> policies = adminService.getAllInsurancePolicies();
    	        return ResponseEntity.ok(policies);
    	    }
    	    
    	    @GetMapping("/{planId}")
    	    public ResponseEntity<InsurancePlan> getInsurancePlanById(@PathVariable Long planId) {
    	        Optional<InsurancePlan> plan = adminService.findById(planId);
    	        return plan.map(ResponseEntity::ok)
    	                   .orElseGet(() -> ResponseEntity.status(404).build());
    	    }
    	    
    	    // Search Insurance Plan by Name
    	    @Operation(summary = "Search Insurance Plan by Name")
    	    @GetMapping("/searchByName")
    	    public ResponseEntity<List<InsurancePlanDTO>> getInsurancePlansByName(@RequestParam String name) {
    	        logger.info("Searching for insurance plans by name: {}", name);
    	        List<InsurancePlanDTO> insurancePlans = adminService.findInsurancePlansByName(name);
    	        return ResponseEntity.ok(insurancePlans);
    	    }
    	    
    	    @Operation(summary = "Deactivate Insurance Plan by ID")
    	    @DeleteMapping("/deactivatePlan/{id}")
    	    public ResponseEntity<String> deactivateInsurancePlan(
    	            @PathVariable("id") Long id) {
    	    	logger.info("Deactivating insurance plan with ID: {}", id);
//    	    	boolean isDeactivated = adminService.deactivateInsurancePlan(id);
//
//    	        if (isDeactivated) {
//    	            logger.info("Insurance plan with ID {} successfully deactivated", id);
//
//    	            return ResponseEntity.ok("Plan deactivated");
//    	        } else {
//    	            logger.warn("Insurance plan with ID {} not found for deactivation", id);
//
//    	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found");
//    	        }
    	    	 adminService.deactivateInsurancePlan(id);
    	         return ResponseEntity.ok("Insurance Plan and associated schemes have been deactivated.");
}
    	    
    	    @Operation(summary = "Activate Insurance Plan by ID")
    	    @PutMapping("/activatePlan/{id}")
    	    public ResponseEntity<String> activateInsurancePlan(@PathVariable("id") Long id) {
    	        logger.info("Activating insurance plan with ID: {}", id);

//    	        boolean isActivated = adminService.activateInsurancePlan(id);
//
//    	        if (isActivated) {
//    	            logger.info("Insurance plan with ID {} successfully activated", id);
//
//    	            return ResponseEntity.ok("Plan activated");
//    	        } else {
//    	            logger.warn("Insurance plan with ID {} not found for activation", id);
//    	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found");
//    	        }
    	        
    	        adminService.activateInsurancePlan(id);
    	        return ResponseEntity.ok("Insurance Plan and associated schemes have been activated.");
    	    }

    	    @Operation(summary = "Activate Insurance Policy by ID")
    	    @PutMapping("/activatePolicy/{id}")
    	    public ResponseEntity<String> activateInsurancePolicy(@PathVariable("id") Long id) {
    	        logger.info("Activating insurance policy with ID: {}", id);

//    	        boolean isActivated = adminService.activateInsurancePolicy(id);
//
//    	        if (isActivated) {
//    	        	logger.info("Insurance policy with ID {} successfully activated", id);
//
//    	            return ResponseEntity.ok("Policy activated");
//    	        } else {
//    	            logger.warn("Insurance policy with ID {} not found for activation", id);
//
//    	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Policy not found");
//    	        }
    	        adminService.activateInsurancePolicy(id);
    	        return ResponseEntity.ok("Insurance Plan and associated schemes have been activated.");
    	    }

    	    @Operation(summary = "Deactivate Insurance Policy by ID")
    	    @DeleteMapping("/deactivatePolicy/{id}")
    	    public ResponseEntity<String> deactivateInsurancePolicy(@PathVariable("id") Long id) {
    	        logger.info("Deactivating insurance policy with ID: {}", id);

    	        boolean isDeactivated = adminService.deactivateInsurancePolicy(id);

    	        if (isDeactivated) {
    	            logger.info("Insurance policy with ID {} successfully deactivated", id);

    	            return ResponseEntity.ok("Policy deactivated");
    	        } else {
    	            logger.warn("Insurance policy with ID {} not found for deactivation", id);
    	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Policy not found");
    	        }
 
}
    	    @PutMapping("/verifyAgent/{agentId}")
  	      public ResponseEntity<String> verifyAgent(@PathVariable Long agentId) {
    	    	logger.info("Verifying agent with ID: {}", agentId);

  	          String response = adminService.verifyAgent(agentId);
  	        logger.info("Verification response for agent ID {}: {}", agentId, response);

  	          return new ResponseEntity<>(response, HttpStatus.OK);
  	      }
    	  
//    	    @PostMapping("/approveClaim/{claimId}")
//    	    public ResponseEntity<String> approveClaim(@PathVariable Long claimId,ClaimResponseDto claimDto){
//    	    String result=adminService.approveAgentClaim(claimId, claimDto);	
//    	    return new ResponseEntity<>(result,HttpStatus.OK);
//    	    }
    	    @PostMapping("/approveClaim/{claimId}")
    	      public ResponseEntity<String> approveClaim(@PathVariable Long claimId, @RequestBody ClaimResponseDto claimDto) {
    	          String result = adminService.approveAgentClaim(claimId,claimDto);
    	          return new ResponseEntity<>(result, HttpStatus.OK);
    	      }
    	    @PostMapping("/approveCustomerClaim/{claimId}")
    	    public ResponseEntity<String> approveCustomerClaim(@PathVariable Long claimId, @RequestBody ClaimResponseDto claimDto) {
    	        String result = adminService.approveCustomerClaim(claimId, claimDto);
    	        return new ResponseEntity<>(result, HttpStatus.OK);
    	    }
    	    
    	    @GetMapping("/payment-tax")
    	      public ResponseEntity<PaymentTax> getPaymentTax() {
    	          PaymentTax paymentTax = adminService.getPaymentTax();
    	          return ResponseEntity.ok(paymentTax);
    	      }

    	      // Set Payment Tax
    	      @PostMapping("/payment-tax")
    	      public ResponseEntity<String> setPaymentTax(@RequestBody PaymentTax paymentTax) {
    	          adminService.setPaymentTax(paymentTax.getPaymentTax());
    	          return ResponseEntity.ok("Payment tax updated successfully");
    	      }
    	      
    	      
    	      
    	      @GetMapping("/customer/policies/{policyId}/installment-amount")
    	      public ResponseEntity<Double> getInstallmentAmount(@PathVariable long policyId) {
    	          double installmentAmount = adminService.getInstallmentAmountByPolicyId(policyId);
    	          return ResponseEntity.ok(installmentAmount);
    	      }
    	      
    	      
    		    @GetMapping("/get-customer-by-id/{id}")
    		    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable("id") long customerId) {
    		        CustomerResponseDto customerResponseDto = customerService.getCustomerById(customerId);
    		        return new ResponseEntity<>(customerResponseDto, HttpStatus.OK);
    		    }

    		    @GetMapping("/search-by-name")
    		    public ResponseEntity<PagedResponse<CustomerResponseDto>> searchCustomersByName(
    		            @RequestParam("name") String name,
    		            @RequestParam(value = "page", defaultValue = "0") int page,
    		            @RequestParam(value = "size", defaultValue = "10") int size) {

    		        PagedResponse<CustomerResponseDto> pagedResponse = customerService.searchCustomersByName(name, page, size);
    		        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    		    }

    		    @GetMapping("/search-by-active-status")
    		    public ResponseEntity<PagedResponse<CustomerResponseDto>> searchCustomersByActiveStatus(
    		            @RequestParam("active") boolean active,
    		            @RequestParam(value = "page", defaultValue = "0") int page,
    		            @RequestParam(value = "size", defaultValue = "10") int size) {

    		        PagedResponse<CustomerResponseDto> pagedResponse = customerService.searchCustomersByActiveStatus(active, page, size);
    		        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    		    }
    		    @GetMapping("/claims")
    		    public ResponseEntity<PagedResponse<ClaimDto>> getAllClaims(
    		            @RequestParam(value = "page", defaultValue = "0") int page,
    		            @RequestParam(value = "size", defaultValue = "10") int size) {
    		        
    		        PagedResponse<ClaimDto> claims = claimService.getAllClaims(page, size);
    		        return ResponseEntity.ok(claims);
    		    }

    		    @PostMapping("/claims/{claimId}/approve")
    		    public ResponseEntity<String> approveClaim(@PathVariable Long claimId,@RequestParam String remark) {
    		        claimService.approveClaim(claimId,remark);
    		        return ResponseEntity.ok("Claim approved.");
    		    }

    		    @PostMapping("/claims/{claimId}/reject")
    		    public ResponseEntity<String> rejectClaim(@PathVariable Long claimId,@RequestParam String remark) {
    		        claimService.rejectClaim(claimId,remark);
    		        return ResponseEntity.ok("Claim rejected.");
    		    }  
//    		    
//    		    @Operation(summary = "Get all DocumentTypes")
//    		    @GetMapping("/document-types")
//    		    public ResponseEntity<List<String>> getDocumentTypes() {
//    		        List<String> documentTypes = Arrays.stream(DocumentType.values())
//    		                .map(Enum::name)
//    		                .collect(Collectors.toList());
//    		        return ResponseEntity.ok(documentTypes);
//    		    }
    		    @Operation(summary = "Get all DocumentTypes")
    		    @GetMapping("/document-types")
    		    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
    		        List<DocumentType> documentTypes = documentTypeService.findAll();
    		        return ResponseEntity.ok(documentTypes);
    		    }
    		 // Get claim status by policyId
    		    @GetMapping("/policy/{policyId}/claim-status")
    		    public ResponseEntity<Optional<Claim>> getClaimStatus(@PathVariable Long policyId){//, @RequestParam Long customerId) {
    		        Optional<Claim> claimStatus = claimService.getClaimStatusByPolicyId(policyId);//, customerId);
    		        if (claimStatus != null) {
    		            return ResponseEntity.ok(claimStatus);
    		        } else {
    		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    		        }
    		    }
    		    
    		    
    		    @GetMapping("/employee-count")
    		    public ResponseEntity<Long> getEmployeeCount() {
    		        return ResponseEntity.ok(adminService.getEmployeeCount());
    		    }

    		    @GetMapping("/agent-count")
    		    public ResponseEntity<Long> getAgentCount() {
    		        return ResponseEntity.ok(adminService.getAgentCount());
    		    }

    		    @GetMapping("/insurance-plan-count")
    		    public ResponseEntity<Long> getInsurancePlanCount() {
    		        return ResponseEntity.ok(adminService.getInsurancePlanCount());
    		    }

    		    @GetMapping("/insurance-scheme-count")
    		    public ResponseEntity<Long> getInsuranceSchemeCount() {
    		        return ResponseEntity.ok(adminService.getInsuranceSchemeCount());
    		    }

    		    @GetMapping("/city-count")
    		    public ResponseEntity<Long> getCityCount() {
    		        return ResponseEntity.ok(adminService.getCityCount());
    		    }

    		    @GetMapping("/state-count")
    		    public ResponseEntity<Long> getStateCount() {
    		        return ResponseEntity.ok(adminService.getStateCount());
    		    }

    		    @GetMapping("/customer-count")
    		    public ResponseEntity<Long> getCustomerCount() {
    		        return ResponseEntity.ok(adminService.getCustomerCount());
    		    }

    		    @GetMapping("/tax-settings-count")
    		    public ResponseEntity<Long> getTaxSettingsCount() {
    		        return ResponseEntity.ok(adminService.getTaxSettingsCount());
    		    }
    		    
    		    @DeleteMapping("/deactivateScheme/{id}") 
    	          public ResponseEntity<String> deactivateScheme(@PathVariable("id") Long id) { 
    	              try { 
    	                  adminService.deactivateScheme(id); 
    	                  return new ResponseEntity<>("Scheme deactivated successfully", HttpStatus.OK); 
    	              } catch (IllegalArgumentException e) { 
    	                  return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
    	              } catch (Exception e) { 
    	                  return new ResponseEntity<>("Error deactivating scheme: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); 
    	              } 
    	          }
    		    
    		    @PutMapping("/activateScheme/{id}") 
    	          public ResponseEntity<String> activateScheme(@PathVariable("id") Long id) { 
    	              try { 
    	                  adminService.activateScheme(id); 
    	                  return new ResponseEntity<>("Scheme activated successfully", HttpStatus.OK); 
    	              } catch (IllegalArgumentException e) { 
    	                  return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
    	              } catch (Exception e) { 
    	                  return new ResponseEntity<>("Error activating scheme: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); 
    	              } 
    	          } 
    		    
    		    
    		    @PutMapping("/withdrawals/{withdrawalId}/approval")
    		    public ResponseEntity<String> approveWithdrawalRequest(@PathVariable long withdrawalId) {
    		     withdrawalRequestService.approveWithdrawal(withdrawalId);
    		     return ResponseEntity.ok("Withdrawal request approved and refund processed successfully.");
    		    }
    		    
    		    @PutMapping("/withdrawal/{withdrawalId}/reject")
    		    public ResponseEntity<String> rejectWithdrawalRequest(@PathVariable long withdrawalId) {
    		     withdrawalRequestService.rejectWithdrawal(withdrawalId);
    		     return ResponseEntity.ok("Withdrawal request rejected successfully.");
    		    }
    		    
    		    
    		    @GetMapping("/withdrawal-requests")
    		    public ResponseEntity<List<WithdrawalRequestDto>> getAllWithdrawalRequests() {
    		     List<WithdrawalRequestDto> withdrawalRequests = withdrawalRequestService.getAllWithdrawalRequests();
    		     return ResponseEntity.ok(withdrawalRequests);
    		    }
}