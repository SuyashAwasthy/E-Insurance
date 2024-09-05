package com.techlabs.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.StateRequest;
import com.techlabs.app.dto.StateResponse;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/E-Insurance/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Add Employee")
	@PostMapping("addEmployee")
	public ResponseEntity<String> registerEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
		return new ResponseEntity<String>(adminService.registerEmployee(employeeRequestDto),
				HttpStatus.ACCEPTED);
	}
    
    @Operation(summary = "Add Agent")
	@PostMapping("addAgent")
	public ResponseEntity<String> registerAgent(@RequestBody AgentRequestDto agentRequestDto) {
		return new ResponseEntity<String>(adminService.registerAgent(agentRequestDto),
				HttpStatus.ACCEPTED);
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
      @GetMapping("/employee/{employeeId}")
      public ResponseEntity<EmployeeResponseDto> viewEmployeebyId(@PathVariable(name = "employeeId") long employeeId) {
      return new ResponseEntity<EmployeeResponseDto>(adminService.findEmployeeByid(employeeId), HttpStatus.OK);
    }

      
      @GetMapping("/agent/{agentId}")
      public ResponseEntity<AgentResponseDto> viewAgentById(@PathVariable(name = "agentId") long agentId) {
          return new ResponseEntity<AgentResponseDto>(adminService.findAgentById(agentId), HttpStatus.OK);
      }
      
//      @PutMapping("/agent/{agentId}")
//      public ResponseEntity<AgentResponseDto> updateAgentById(
//              @PathVariable(name = "agentId") long agentId,
//              @RequestBody AgentRequestDto agentRequestDto) {
//    	  System.out.println("Updating agent");
//          AgentResponseDto updatedAgent = adminService.updateAgentById(agentId, agentRequestDto);
//          return new ResponseEntity<AgentResponseDto>(updatedAgent, HttpStatus.OK);
//      }
//      
      @Operation(summary = "Update Agent")
      @PutMapping("updateAgent/{agentId}")
      public ResponseEntity<String> updateAgent(
              @PathVariable Long agentId,
              @RequestBody AgentRequestDto agentRequestDto) {
          return new ResponseEntity<String>(adminService.updateAgent(agentId, agentRequestDto), HttpStatus.OK);
      }
//      @PutMapping("/employee/{employeeId}")
//      public ResponseEntity<EmployeeResponseDto> updateEmployeeById(
//              @PathVariable(name = "employeeId") long employeeId,
//              @RequestBody EmployeeRequestDto employeeRequestDto) {
//          EmployeeResponseDto updatedEmployee = adminService.updateEmployeeById(employeeId, employeeRequestDto);
//          return new ResponseEntity<EmployeeResponseDto>(updatedEmployee, HttpStatus.OK);
//      }
      @Operation(summary = "Update Employee")
      @PutMapping("updateEmployee/{employeeId}")
      public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeRequestDto employeeRequestDto) {
          return new ResponseEntity<>(adminService.updateEmployee(employeeId, employeeRequestDto), HttpStatus.OK);
      }
    @GetMapping("/getAllAgents")
	public ResponseEntity<PagedResponse<AgentResponseDto>> getAllAgents(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<AgentResponseDto> agents = adminService.getAllAgents(page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<AgentResponseDto>>(agents, HttpStatus.ACCEPTED);
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
    @GetMapping("/states")
    public ResponseEntity<PagedResponse<StateResponse>> getAllStates(
    	      @RequestParam(name = "page", defaultValue = "0") int page,
    	      @RequestParam(name = "size", defaultValue = "5") int size,
    	      @RequestParam(name = "sortBy", defaultValue = "stateId") String sortBy,
    	      @RequestParam(name = "direction", defaultValue = "asc") String direction)  {
    	    return new ResponseEntity<PagedResponse<StateResponse>>(
    	        adminService.getAllStates(page, size, sortBy, direction), HttpStatus.OK);

    	  }
    	    
    	    @DeleteMapping("/state/{id}")
    	    public ResponseEntity<String>deactivateState(@PathVariable(name="id") long id){
    	      return new ResponseEntity<String>(adminService.deactivateStateById(id),HttpStatus.OK);
    	    }
    	    
    	    @PutMapping("/state/{id}")
    	    public ResponseEntity<String>activateState(@PathVariable(name="id") long id){
    	      return new ResponseEntity<String>(adminService.activateStateById(id),HttpStatus.OK);
    	    }
    	    
    	    @PostMapping("/create-city")
    	    public ResponseEntity<String> createCity(@RequestBody CityRequest cityRequest){
    	      
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
    	      return new ResponseEntity<String>(adminService.deactivateCity(id),HttpStatus.OK);
    	      
    	    }
    	    
    	    @GetMapping("/city/{id}")
    	    public ResponseEntity<CityResponse>getCityById(@PathVariable(name="id") long id){
    	      return new ResponseEntity<CityResponse>(adminService.getCityById(id),HttpStatus.OK);
    	    }
    	    
    	    @PutMapping("/city/{id}")
    	    public ResponseEntity<String>activateCity(@PathVariable(name="id") long id){
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
    	    

    	    @Operation(summary = "Create Insurance Plan")
    		@PostMapping("/createPlan")
    		public ResponseEntity<String> createInsurancePlan(@RequestBody InsurancePlanDTO insurancePlanDto) {
    			logger.info("Creating insurance plan: {}", insurancePlanDto);
    			String response = adminService.createInsurancePlan(insurancePlanDto);
    			return new ResponseEntity<>(response, HttpStatus.CREATED);
    		}

    		@Operation(summary = "Create Insurance Scheme")
    		@PostMapping("/createScheme")
    		public ResponseEntity<String> createInsuranceScheme(@RequestBody InsuranceSchemeDto insuranceSchemeDto) {
    			logger.info("Creating insurance scheme: {}", insuranceSchemeDto);
    			String response = adminService.createInsuranceScheme(insuranceSchemeDto);
    			return new ResponseEntity<>(response, HttpStatus.CREATED);
    		}

    		@Operation(summary = "Create Insurance Policy")
    		@PostMapping("/createPolicy")
    		public ResponseEntity<String> createInsurancePolicy(@RequestBody InsurancePolicyDto insurancePolicyDto) {
    			logger.info("Creating insurance policy: {}", insurancePolicyDto);
    			String response = adminService.createInsurancePolicy(insurancePolicyDto);
    			return new ResponseEntity<>(response, HttpStatus.CREATED);
    		}
    		
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
    	            String response = adminService.updateInsurancePlan(schemeId, insuranceSchemeDto);
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
    	    public ResponseEntity<List<InsurancePlanDTO>> getAllInsurancePlans() {
    	        List<InsurancePlanDTO> plans = adminService.getAllInsurancePlans();
    	        return ResponseEntity.ok(plans);
    	    }

    	    @Operation(summary = "Get all Insurance Schemes")
    	    @GetMapping("/getAllSchemes")
    	    public ResponseEntity<List<InsuranceSchemeDto>> getAllInsuranceSchemes() {
    	        List<InsuranceSchemeDto> schemes = adminService.getAllInsuranceSchemes();
    	        return ResponseEntity.ok(schemes);
    	    }

    	    @Operation(summary = "Get all Insurance Policies")
    	    @GetMapping("/getAllPolicies")
    	    public ResponseEntity<List<InsurancePolicyDto>> getAllInsurancePolicies() {
    	        List<InsurancePolicyDto> policies = adminService.getAllInsurancePolicies();
    	        return ResponseEntity.ok(policies);
    	    }
    	    

}