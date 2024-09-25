package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Payment;

import lombok.Data;

@Data
public class ClaimDto {
	private Long id;
    private String claimedStatus;
    private Long policyId;
    private Long customerId; 
    private String ifscCode;
    private String branchName;
    private String bankAccountId;
    private String bankName;
    private Double claimAmount;
    private LocalDate maturityDate;
    private InsurancePlan insuranceScheme;
    private int totalInstallements;
    private double amount;
    private double totalAmountPid;
    private Long totalPaidInstallments;
    private LocalDate issuedDate;
    private String remark;
    private String claimStatus;
    
    
    public ClaimDto(Claim claim, List<Payment> fpayment) {
        this.id = claim.getId();
        this.claimedStatus = claim.getClaimedStatus();
        this.policyId = claim.getPolicy().getInsuranceId();//claim.getPolicy() != null ? claim.getPolicy().getInsuranceId() : null; // Ensure policy ID is fetched
      //  this.customerId = claim.getCustomer() != null ? claim.getCustomer().getCustomerId() : null; // Assuming getCustomer() exists
        this.ifscCode=claim.getIfscCode();
        this.bankName=claim.getBankName();
        this.branchName=claim.getBranchName();
        this.bankAccountId=claim.getBankAccountId();
        this.customerId=claim.getPolicy().getCustomers().get(0).getCustomerId();
        this.maturityDate=claim.getPolicy().getMaturityDate();
        this.issuedDate=claim.getPolicy().getIssuedDate();
        this.insuranceScheme=claim.getPolicy().getInsuranceScheme().getInsurancePlan();
        this.totalInstallements=claim.getPolicy().getPolicyTerm() * 12 / claim.getPolicy().getInstallmentPeriod();
        this.amount=claim.getPolicy().getPremiumAmount();
        this.totalAmountPid=claim.getPolicy().getTotalAmountPaid();
        this.totalPaidInstallments = calculateInstallments(fpayment, claim);
        this.remark=claim.getRemark();
        this.claimStatus=claim.getClaimedStatus();
    }
    
    public ClaimDto(String claimedStatus2, String bankName2, String branchName2, String bankAccountId2,
			String ifscCode2) {
		// TODO Auto-generated constructor stub
	}

	public ClaimDto(String claimedStatus2, String bankName2, String branchName2, String bankAccountId2,
			String ifscCode2, String claimedStatus3) {
		// TODO Auto-generated constructor stub
	}

	private Long calculateInstallments(List<Payment> fpayment, Claim claim) {
        int totalInstallments = claim.getPolicy().getPolicyTerm() * 12 / claim.getPolicy().getInstallmentPeriod();
        LocalDate installmentStartDate = claim.getPolicy().getIssuedDate();
        double installmentAmount = claim.getPolicy().getPremiumAmount() / totalInstallments;

        int totalPaidInstallments = 0;

        System.out.println("Total Installments: " + totalInstallments);
        System.out.println("Installment Start Date: " + installmentStartDate);
        System.out.println("Installment Amount: " + installmentAmount);
        
        // Iterate through total installments
        for (int i = 1; i <= totalInstallments; i++) {
            LocalDate installmentDate = installmentStartDate.plusMonths((long) claim.getPolicy().getInstallmentPeriod() * (i - 1));
            System.out.println("Checking Installment Date: " + installmentDate);
            
         // Check if a payment has been made for this installment date
            boolean isPaid = fpayment.stream()
                .anyMatch(payment -> payment.getPaymentDate().toLocalDate().equals(installmentDate)
                    && "Paid".equalsIgnoreCase(payment.getPaymentStatus())); // Ignore case sensitivity


            // If a payment is found, increment the paid installments count
            if (isPaid) {
                totalPaidInstallments++;
                System.out.println("Installment " + i + " is Paid.");
            } else {
                System.out.println("Installment " + i + " is Unpaid.");
            }
        }

        // Log the total paid installments count
        System.out.println("Total Paid Installments: " + totalPaidInstallments);

        return (long) totalPaidInstallments; // Return the total number of paid installments
    }


//    private Long calculateInstallments(List<Payment> fpayment, Claim claim) {
//        List<PaymentDto> paymentDtos = new ArrayList<>();
//        int totalInstallments = claim.getPolicy().getPolicyTerm() * 12 / claim.getPolicy().getInstallmentPeriod();
//        LocalDate installmentStartDate = claim.getPolicy().getIssuedDate();
//        double installmentAmount = claim.getPolicy().getPremiumAmount() / totalInstallments;
//
//        // Create a map of payments by their date
//        Map<LocalDate, Payment> paymentMap = fpayment.stream()
//            .collect(Collectors.toMap(
//                payment -> payment.getPaymentDate().toLocalDate(),
//                payment -> payment,
//                (existing, replacement) -> replacement
//            ));
//
//        int totalPaidInstallments = 0;
//        int n = fpayment.size(); // Number of recorded payments
//
//        // Iterate through total installments and set payment details
//        for (int i = 1; i <= totalInstallments; i++) {
//            PaymentDto paymentDto = new PaymentDto();
//            paymentDto.setInstallmentNumber(i);
//
//            // Calculate the installment date based on policy issue date and installment period
//            LocalDate installmentDate = installmentStartDate.plusMonths((long) claim.getPolicy().getInstallmentPeriod() * (i - 1));
//            paymentDto.setInstallmentDate(installmentDate);
//            paymentDto.setInstallmentAmount(installmentAmount);
//            paymentDto.setPaymentStatus("Unpaid"); // Default status
//
//            // Check if the installment has been paid
//            if (i - 1 < n && "Paid".equals(fpayment.get(i - 1).getPaymentStatus())) {
//                paymentDto.setPaymentStatus(fpayment.get(i - 1).getPaymentStatus());
//                paymentDto.setPaidDate(fpayment.get(i - 1).getPaymentDate());
//                totalPaidInstallments++; // Increment the count of paid installments
//            }
//
//            paymentDtos.add(paymentDto); // Add to the list
//        }
//
//        return (long) totalPaidInstallments; // Return the total number of paid installments
//    }

}
