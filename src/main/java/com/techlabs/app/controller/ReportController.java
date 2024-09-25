package com.techlabs.app.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.techlabs.app.service.ReportService;

@RestController 
@RequestMapping("/E-Insurance/reports") 
public class ReportController { 
 
    @Autowired 
    private ReportService reportService; 
 
    @GetMapping("/agent-report") 
    public ResponseEntity<byte[]> generateAgentReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, 
                                                      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { 
        // Convert LocalDate to LocalDateTime for the start of the day and end of the day 
//        LocalDate startDateTime = startDate.atStartOfDay(); // Start of the day 
//        LocalDate endDateTime = endDate.atTime(LocalTime.MAX); // End of the day 
 
        byte[] pdfContent = reportService.generateAgentReport(startDate, endDate); 
        HttpHeaders headers = new HttpHeaders(); 
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=agent_report.pdf"); 
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf"); 
 
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK); 
    } 
 
    @GetMapping("/customer-report") 
    public ResponseEntity<byte[]> generateCustomerReport(@RequestParam("startDate") LocalDate startDate, 
                                                         @RequestParam("endDate") LocalDate endDate) throws DocumentException { 
        byte[] pdfContent = reportService.generateCustomerReport(startDate, endDate); 
        HttpHeaders headers = new HttpHeaders(); 
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customer_report.pdf"); 
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf"); 
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK); 
    } 
    @GetMapping("/agent-commission-report") 
    public ResponseEntity<byte[]> generateAgentCommissionReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, 
                                                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { 
        byte[] pdfContent = reportService.generateAgentCommissionReport(startDate, endDate); 
        HttpHeaders headers = new HttpHeaders(); 
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=agent_commission_report.pdf"); 
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf"); 
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK); 
    } 
 
    @GetMapping("/policy-payment-report") 
    public ResponseEntity<byte[]> generatePolicyPaymentReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, 
                                                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { 
        byte[] pdfContent = reportService.generatePolicyPaymentReport(startDate, endDate); 
        HttpHeaders headers = new HttpHeaders(); 
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=policy_payment_report.pdf"); 
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf"); 
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK); 
    } 
 
}
