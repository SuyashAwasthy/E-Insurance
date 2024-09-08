package com.techlabs.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {

	//byte[] generateAgentReport(LocalDateTime startDateTime, LocalDateTime endDateTime);

	byte[] generateCustomerReport(LocalDate startDate, LocalDate endDate);

	byte[] generateAgentCommissionReport(LocalDate startDate, LocalDate endDate);

	byte[] generatePolicyPaymentReport(LocalDate startDate, LocalDate endDate);

	byte[] generateAgentReport(LocalDate startDateTime, LocalDate endDateTime);

}
