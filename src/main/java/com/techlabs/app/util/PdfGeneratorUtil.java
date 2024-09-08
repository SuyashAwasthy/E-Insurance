package com.techlabs.app.util;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Payment;

public class PdfGeneratorUtil {
	
	public static byte[] createAgentReportPdf(List<Agent> agents) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add Title with corrected font initialization
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Agent Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Empty line for spacing

            // Add Table with appropriate columns for agent fields
            PdfPTable table = new PdfPTable(5); // Adjust column count as needed
            table.addCell("Agent ID");
            table.addCell("First Name");
            table.addCell("Last Name");
            table.addCell("Phone Number");
            table.addCell("Verified");

            // Populate table with agent data
            for (Agent agent : agents) {
                table.addCell(String.valueOf(agent.getAgentId()));
                table.addCell(agent.getFirstName());
                table.addCell(agent.getLastName());
                table.addCell(agent.getPhoneNumber());
                table.addCell(agent.isVerified() ? "Yes" : "No");
            }

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate agent report PDF");
        }
    }


//    public static byte[] createCustomerReportPdf(List<Customer> customers) {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            Document document = new Document();
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            // Add Title with corrected font initialization
//            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//            Paragraph title = new Paragraph("Customer Report", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(new Paragraph(" ")); // Empty line for spacing
//
//            // Add Table with appropriate columns for customer fields
//            PdfPTable table = new PdfPTable(7); // Adjust column count based on the number of fields
//            table.addCell("Customer ID");
//            table.addCell("First Name");
//            table.addCell("Last Name");
//            table.addCell("Date of Birth");
//            table.addCell("Phone Number");
//            table.addCell("City");
//            table.addCell("Verified");
//
//            // Populate table with customer data
//            for (Customer customer : customers) {
//                table.addCell(String.valueOf(customer.getCustomerId()));
//                table.addCell(customer.getFirstName());
//                table.addCell(customer.getLastName());
//                table.addCell(customer.getDob() != null ? customer.getDob().toString() : "N/A");
//              //  table.addCell(customer.getPhoneNumber());
//                table.addCell(customer.getCity() != null ? customer.getCity().getCity_name() : "N/A");
//                table.addCell(customer.isVerified() ? "Yes" : "No");
//            }
//
//            document.add(table);
//            document.close();
//            return outputStream.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to generate customer report PDF");
//        }
//    }
	
	
	public static byte[] createCustomerReportPdf(List<Customer> customers) {
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        Document document = new Document();
	        PdfWriter.getInstance(document, outputStream);
	        document.open();

	        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	        Paragraph title = new Paragraph("Customer Report", titleFont);
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);
	        document.add(new Paragraph(" "));

	        PdfPTable table = new PdfPTable(7); // Adjust the number of columns as needed
	        table.addCell("Customer ID");
	        table.addCell("First Name");
	        table.addCell("Last Name");
	        table.addCell("Date of Birth");
	        table.addCell("Phone Number");
	        table.addCell("City");
	        table.addCell("Verified");

	        for (Customer customer : customers) {
	            table.addCell(String.valueOf(customer.getCustomerId()));
	            table.addCell(customer.getFirstName() != null ? customer.getFirstName() : "N/A");
	            table.addCell(customer.getLastName() != null ? customer.getLastName() : "N/A");
	            table.addCell(customer.getDob() != null ? customer.getDob().toString() : "N/A");
	            table.addCell(String.valueOf(customer.getPhoneNumber()));
	            table.addCell(customer.getCity() != null && customer.getCity().getCity_name() != null ? customer.getCity().getCity_name() : "N/A");
	            table.addCell(customer.isVerified() ? "Yes" : "No");
	        }

	        document.add(table);
	        document.close();
	        return outputStream.toByteArray();
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to generate customer report PDF");
	    }
	}

	
//    public static byte[] createAgentCommissionReportPdf(List<Commission> commissions) {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            Document document = new Document();
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//            Paragraph title = new Paragraph("Agent Commission Report", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(new Paragraph(" "));
//
//            PdfPTable table = new PdfPTable(4);
//            table.addCell("Agent Name");
//            table.addCell("Policy");
//            table.addCell("Commission Amount");
//            
//            table.addCell("Date");
//
//            for (Commission commission : commissions) {
//                table.addCell(commission.getAgent().getFirstName());
//                table.addCell(commission.getInsurancePolicy().getPolicyStatus());
//                table.addCell(commission.getInsurancePolicy().getClaimAmount());
//                
//                table.addCell(String.valueOf(commission.getAmount()));
//                table.addCell(commission.getDate().toString());
//            }
//
//            document.add(table);
//            document.close();
//            return outputStream.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to generate Agent Commission Report PDF");
//        }
//    }
	
	public static byte[] createAgentCommissionReportPdf(List<Commission> commissions) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Agent Commission Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Table setup
            PdfPTable table = new PdfPTable(6); // Updated number of columns
            table.addCell("Agent Name");
            table.addCell("Commission Amount");
            table.addCell("Policy ID");
            table.addCell("Customer ID");
            table.addCell("Policy Status");
            table.addCell("Date");

            // Add rows
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Commission commission : commissions) {
                String agentName = commission.getAgent().getFirstName() + " " + commission.getAgent().getLastName();
                String policyId = String.valueOf(commission.getInsurancePolicy().getInsuranceId());
                String customerId = String.valueOf(commission.getInsurancePolicy().getCustomers());
                String policyStatus = commission.getInsurancePolicy().getPolicyStatus();
                
                table.addCell(agentName);
                table.addCell(String.format("%.2f", commission.getAmount()));
                table.addCell(policyId);
                table.addCell(customerId);
                table.addCell(policyStatus);
                table.addCell(commission.getDate().format(formatter));
            }

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate Agent Commission Report PDF");
        }
    }
	
    public static byte[] createPolicyPaymentReportPdf(List<Payment> payments) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Policy Payment Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);  // Add more columns
            table.addCell("Policy Name");
            table.addCell("Customer Name");
            table.addCell("Payment Amount");
            table.addCell("Payment Type");
            table.addCell("Tax");
            table.addCell("Date");

            for (Payment payment : payments) {
              //  table.addCell(payment.getPolicy().getId());
                //table.addCell(payment.getCustomer().getFirstName());
                table.addCell(String.valueOf(payment.getAmount()));
                table.addCell(payment.getPaymentType());
                table.addCell(String.valueOf(payment.getTax()));
                table.addCell(payment.getPaymentDate().toString());
            }

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate Policy Payment Report PDF");
        }
    }




}
