package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.PaymentDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.util.PagedResponse;

public interface CustomerService {

	void addCustomer(RegisterDto registerDto);

	//List<Customer> getAllCustomers();

	String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto);

	InsurancePolicyDto buyPolicy(InsurancePolicyDto accountRequestDto, long customerId);

	InsurancePolicyDto buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId);

	String claimPolicy(ClaimRequestDto claimRequestDto, long customerId);

	String requestPolicyCancellation(CancellationRequestDto cancellationRequest, Long customerId);

	String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId);

	PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size);

	String buyPolicy(InsurancePolicyDto accountRequestDto, Customer customerId);

	String buyPolicy(InsurancePolicyDto accountRequestDto, Long customerId);

	void changePassword(Long employeeId, ChangePasswordDto changePasswordDto);

	CustomerResponseDto findCustomerByid(long customerId);

	void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto);

	InsurancePolicyDto getPolicyByid(Long customerId, Long policyId);

	//List<InsurancePolicyDto> getAllPoliciesByCustomerId(Long customerId);

	PagedResponse<InsurancePolicyDto> getAllPoliciesByCustomerId(Long customerId, int page, int size);

	List<Customer> findByCityCityId(Long cityId);

	Customer registerCustomer(Customer customer);

	CustomerDTO getCustomerDetailsByPolicyId(long policyId);

	CustomerDTO getCustomerDetailsByCustomerId(Long customerId);

	List<Payment> getPaymentsByPolicyId(Long policyId);

	List<Payment> getPaymentsByCustomerAndPolicy(Long customerId, Long policyId);

	CustomerResponseDto getCustomerById(long customerId);

	PagedResponse<CustomerResponseDto> searchCustomersByName(String name, int page, int size);

	PagedResponse<CustomerResponseDto> searchCustomersByActiveStatus(boolean active, int page, int size);

	//List<PaymentDto> getInstallmentsByPolicyId(Long policyId);

	ClaimResponseDto processClaim(ClaimRequestDto claimRequestDto, Long customerId);

	CustomerDTO checkCustomerStatus(String email);

//	List<CustomerResponseDto> getAllCustomerByAgent();

	Customer findCustomerByEmail(String email);

	PagedResponse<CustomerResponseDto> getAllCustomerByAgent(int page, int size);

	
	//String buyPolicy(PolicyAccountRequestDto accountRequestDto, long customerId);

}
