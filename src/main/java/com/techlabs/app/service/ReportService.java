package com.techlabs.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.itextpdf.text.DocumentException;

public interface ReportService {

	//byte[] generateAgentReport(LocalDateTime startDateTime, LocalDateTime endDateTime);

	byte[] generateCustomerReport(LocalDate startDate, LocalDate endDate) throws DocumentException;

	byte[] generateAgentCommissionReport(LocalDate startDate, LocalDate endDate);

	byte[] generatePolicyPaymentReport(LocalDate startDate, LocalDate endDate);

	byte[] generateAgentReport(LocalDate startDateTime, LocalDate endDateTime);

}
