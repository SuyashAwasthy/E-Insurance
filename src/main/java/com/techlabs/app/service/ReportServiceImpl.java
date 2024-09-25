package com.techlabs.app.service;

import java.time.LocalDate;
import java.io.ByteArrayOutputStream;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.awt.geom.Rectangle;
//import com.stripe.model.Customer;
import com.techlabs.app.entity.Agent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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
    
    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;
    
    

    public ReportServiceImpl(AgentRepository agentRepository, CustomerRepository customerRepository,
			CommissionRepository commissionRepository, PaymentRepository paymentRepository,
			InsurancePolicyRepository insurancePolicyRepository) {
		super();
		this.agentRepository = agentRepository;
		this.customerRepository = customerRepository;
		this.commissionRepository = commissionRepository;
		this.paymentRepository = paymentRepository;
		this.insurancePolicyRepository = insurancePolicyRepository;
	}

	private static final Font BOLD_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
   	private static final Font REGULAR_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
   	private static final Font HEADER_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLUE);
   	private static final Font WATERMARK_FONT = new Font(Font.FontFamily.HELVETICA, 52, Font.BOLD, BaseColor.LIGHT_GRAY);
   	private static final BaseColor HEADER_BG_COLOR = new BaseColor(230, 230, 250);
    
    
	@Override
	public byte[] generateAgentReport(LocalDate startDateTime, LocalDate endDateTime) {
		List<Agent> agents = agentRepository.findAgentsByRegistrationDateBetween(startDateTime, endDateTime);
        return PdfGeneratorUtil.createAgentReportPdf(agents);}

//	@Override
//	public byte[] generateCustomerReport(LocalDate startDate, LocalDate endDate) {
//		List<Customer> customers = customerRepository.findCustomersByRegistrationDateBetween(startDate, endDate);
//		// For each customer, fetch associated insurance policies
//	    Map<Customer, List<InsurancePolicy>> customerPolicies = new HashMap<>();
//	    for (Customer customer : customers) {
//	        List<InsurancePolicy> policies = insurancePolicyRepository.findByCustomerId(customer.getCustomerId());
//	        customerPolicies.put(customer, policies);
//	    }
//
//	    // Pass customer and policy details to PDF generator
//	    return generatePdf(customerPolicies);
//	}
	@Override
	public byte[] generateCustomerReport(LocalDate startDate, LocalDate endDate) throws DocumentException {
	    // Fetch customer details along with their policies during the specified period
	    List<Customer> customers = customerRepository.findCustomersByRegistrationDateBetween(startDate, endDate);

	    // For each customer, fetch associated insurance policies
	    Map<Customer, List<InsurancePolicy>> customerPolicies = new HashMap<>();
	    for (Customer customer : customers) {
	        List<InsurancePolicy> policies = insurancePolicyRepository.findByCustomerId(customer.getCustomerId());
	        customerPolicies.put(customer, policies);
	    }

	    // Pass customer and policy details to PDF generator
	    return generatePdf(customerPolicies);
	}
	
	private byte[] generatePdf(Map<Customer, List<InsurancePolicy>> customerPolicies) throws DocumentException {
	    Document document = new Document();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    PdfWriter.getInstance(document, outputStream);
	    document.open();
	    
	    PdfPCell logoCell = new PdfPCell();  // For logo
	    logoCell.setBorder(Rectangle.OUT_BOTTOM);

	    PdfPCell titleCell = new PdfPCell(new Phrase("Customer Insurance Report"));
	    titleCell.setBorder(Rectangle.OUT_BOTTOM);
	    titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);


	    for (Map.Entry<Customer, List<InsurancePolicy>> entry : customerPolicies.entrySet()) {
	        Customer customer = entry.getKey();
	        List<InsurancePolicy> policies = entry.getValue();

	        // Add customer details to PDF
	        document.add(new Paragraph("Customer First Name: " + customer.getFirstName()));
	        document.add(new Paragraph("Customer Last Name: " + customer.getLastName()));
	        document.add(new Paragraph("Phone Number: " + customer.getPhoneNumber()));
//	        document.add(new Paragraph("Date of Birth: " + customer.getDob()));

	        // Add policy details in a table
	        PdfPTable table = new PdfPTable(7); // Number of columns based on policy fields
	        table.addCell("Policy ID");
	        table.addCell("Issued Date");
	        table.addCell("Maturity Date");
	        table.addCell("Premium Amount");
	        table.addCell("Policy Status");
	        table.addCell("Total Amount Paid");
	        table.addCell("Claim Amount");

	        for (InsurancePolicy policy : policies) {
	            table.addCell(String.valueOf(policy.getInsuranceId()));
	            table.addCell(policy.getIssuedDate().toString());
	            table.addCell(policy.getMaturityDate() != null ? policy.getMaturityDate().toString() : "N/A");
	            table.addCell(String.valueOf(policy.getPremiumAmount()));
	            table.addCell(policy.getPolicyStatus());
	            table.addCell(String.valueOf(policy.getTotalAmountPaid()));
	            table.addCell(String.valueOf(policy.getClaimAmount()));
	        }

	        document.add(table);
	        document.add(new Paragraph(" "));
	    }

	    document.close();
	    return outputStream.toByteArray();
	}

	@Override
	public byte[] generateAgentCommissionReport(LocalDate startDate, LocalDate endDate) {
		 List<Commission> commissions = commissionRepository.findCommissionsByDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
		 System.out.println("Number of commissions: " + commissions.size());
	        return PdfGeneratorUtil.createAgentCommissionReportPdf(commissions);
	   
	}

	@Override
	public byte[] generatePolicyPaymentReport(LocalDate startDate, LocalDate endDate) {
		List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        return PdfGeneratorUtil.createPolicyPaymentReportPdf(payments);
  
	}

}
