package com.techlabs.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CityRequest;
import com.techlabs.app.dto.CityResponse;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.StateRequest;
import com.techlabs.app.dto.StateResponse;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.PaymentTax;
import com.techlabs.app.util.PagedResponse;

import jakarta.validation.Valid;

public interface AdminService{
    String registerEmployee(EmployeeRequestDto employeeRequestDto);
	String registerAgent(AgentRequestDto agentRequestDto);
	String createTaxSetting(TaxSettingRequestDto taxSettingRequestDto);
	PagedResponse<AgentResponseDto> getAllAgents(int page, int size, String sortBy, String direction);
	String createState(StateRequest stateRequest);
	PagedResponse<StateResponse> getAllStates(int page, int size, String sortBy, String direction);
	String deactivateStateById(long id);
	String activateStateById(long id);
	String createCity(CityRequest cityRequest);
	String deactivateCity(long id);
	CityResponse getCityById(long id);
	String activateCity(long id);
	PagedResponse<CityResponse> getAllCities(int page, int size, String sortBy, String direction);
	PagedResponse<EmployeeResponseDto> getAllEmployees(int page, int size, String sortBy, String direction);
	EmployeeResponseDto findEmployeeByid(long employeeId);
	AgentResponseDto findAgentById(long agentId);
	//AgentResponseDto updateAgentById(long agentId, AgentRequestDto agentRequestDto);
	//EmployeeResponseDto updateEmployeeById(long employeeId, EmployeeRequestDto employeeRequestDto);
	String updateEmployee(Long employeeId, EmployeeRequestDto employeeRequestDto);
	String updateAgent(Long agentId, AgentRequestDto agentRequestDto);
	String createInsurancePlan(InsurancePlanDTO insurancePlanDto);
	//String createInsuranceScheme(InsuranceSchemeDto insuranceSchemeDto);
	String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto);
//	String updateInsurancePlan(Long planId, InsurancePlanDTO insurancePlanDto);
//	String updateInsurancePolicy(Long policyId, InsurancePolicyDto insurancePolicyDto);
//	//String updateInsurancePlan(Long schemeId, InsuranceSchemeDto insuranceSchemeDto);
//	String updateInsuranceScheme(Long schemeId, InsuranceSchemeDto insuranceSchemeDto);
	List<InsurancePlanDTO> getAllInsurancePlans();
	//List<InsuranceSchemeDto> getAllInsuranceSchemes();
	List<InsurancePolicyDto> getAllInsurancePolicies();
	String updateInsurancePlan(Long planId, InsurancePlanDTO insurancePlanDto);
	String updateInsurancePolicy(Long policyId, InsurancePolicyDto insurancePolicyDto);
	String updateInsuranceScheme(Long schemeId, InsuranceSchemeDto insuranceSchemeDto);
	
	void deactivateInsurancePlan(Long id);
	void activateInsurancePlan(Long id);
	boolean activateInsurancePolicy(Long id);
	boolean deactivateInsurancePolicy(Long id);
	
	String verifyAgent(Long agentId);
		
	
	// approving the claimm
	String approveAgentClaim(Long claimId, ClaimResponseDto claimDto);
	String approveCustomerClaim(Long claimId, ClaimResponseDto claimDto);
	List<InsuranceSchemeDto> getSchemesByPlan(Long planId);
	void deactivateAgent(Long id);
	PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size);
	String createInsuranceScheme(long insurancePlanId, @Valid InsuranceSchemeDto insuranceSchemeDto);
	PagedResponse<InsurancePlanDTO> getAllPlans(int page, int size, String sortBy, String direction);
	PagedResponse<InsuranceSchemeDto> getAllSchemes(int page, int size, String sortBy, String direction);
	PagedResponse<InsuranceSchemeDto> getAllSchemesByPlanId(Long planId, int page, int size, String sortBy,
			String direction);
	CustomerResponseDto findCustomerByid(long customerId);
	Optional<InsurancePlan> findById(Long planId);

	
void setPaymentTax(Long paymentTax);
    
//    double getInstallmentAmountByPolicyId(long policyId);
	PaymentTax getPaymentTax();
	double getInstallmentAmountByPolicyId(long policyId);
	List<InsurancePlanDTO> findInsurancePlansByName(String name);
	AgentResponseDto getAgentById(Long agentId);
	PagedResponse<AgentResponseDto> getAgentsByActiveStatus(boolean active, int page, int size, String sortBy,
			String direction);
	PagedResponse<EmployeeResponseDto> getEmployeesByActiveStatus(boolean active, int page, int size, String sortBy,
			String direction);
	List<DocumentType> findAll();
	void deactivateCustomer(Long id);
	long getEmployeeCount(); 
    long getAgentCount(); 
    long getInsurancePlanCount(); 
    long getInsuranceSchemeCount(); 
    long getCityCount(); 
    long getStateCount(); 
    long getCustomerCount(); 
    long getTaxSettingsCount();
	void deactivateScheme(Long id);
	void activateScheme(Long id);

		
	
	
}