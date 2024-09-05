package com.techlabs.app.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CityRequest;
import com.techlabs.app.dto.CityResponse;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.StateRequest;
import com.techlabs.app.dto.StateResponse;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.util.PagedResponse;

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
	String createInsuranceScheme(InsuranceSchemeDto insuranceSchemeDto);
	String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto);
	String updateInsurancePlan(Long planId, InsurancePlanDTO insurancePlanDto);
	String updateInsurancePolicy(Long policyId, InsurancePolicyDto insurancePolicyDto);
	String updateInsurancePlan(Long schemeId, InsuranceSchemeDto insuranceSchemeDto);
	String updateInsuranceScheme(Long schemeId, InsuranceSchemeDto insuranceSchemeDto);
	List<InsurancePlanDTO> getAllInsurancePlans();
	List<InsuranceSchemeDto> getAllInsuranceSchemes();
	List<InsurancePolicyDto> getAllInsurancePolicies();
	
	//Insurance type,plan
	
	
	
	//Insurance
	
}