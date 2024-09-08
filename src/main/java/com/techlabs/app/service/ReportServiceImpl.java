package com.techlabs.app.service;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.model.Customer;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.PaymentRepository;
import com.techlabs.app.util.PdfGeneratorUtil;

@Service
public class ReportServiceImpl implements ReportService{
	
	@Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CommissionRepository commissionRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;

	@Override
	public byte[] generateAgentReport(LocalDate startDateTime, LocalDate endDateTime) {
		List<Agent> agents = agentRepository.findAgentsByRegistrationDateBetween(startDateTime, endDateTime);
        return PdfGeneratorUtil.createAgentReportPdf(agents);}

	@Override
	public byte[] generateCustomerReport(LocalDate startDate, LocalDate endDate) {
		List<com.techlabs.app.entity.Customer> customers = customerRepository.findCustomersByRegistrationDateBetween(startDate, endDate);
        return PdfGeneratorUtil.createCustomerReportPdf(customers);
	}

	@Override
	public byte[] generateAgentCommissionReport(LocalDate startDate, LocalDate endDate) {
		 List<Commission> commissions = commissionRepository.findCommissionsByDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
	        return PdfGeneratorUtil.createAgentCommissionReportPdf(commissions);
	   
	}

	@Override
	public byte[] generatePolicyPaymentReport(LocalDate startDate, LocalDate endDate) {
		List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        return PdfGeneratorUtil.createPolicyPaymentReportPdf(payments);
  
	}

}
