package com.techlabs.app.dto;

import lombok.Data;
import java.sql.Date;

import com.techlabs.app.entity.PaymentType;
import com.techlabs.app.entity.PremiumType;

@Data
public class PolicyAccountRequestDto {
	private Long insuranceSchemeId;
	  private Long agentId;
	  private PremiumType premiumType;
	  private Long policyTerm;
	  private Double premiumAmount;
	  
	  //Claim
	  private double claimAmount;

	  private String bankName;

	  private String branchName;

	  private String bankAccountNumber;

	  private String ifscCode;

	  private Date date;

	  // Payment
	  private PaymentType paymentType;

	  private Double amount;

	  private Date paymentDate;

	  private Double tax;

	  private Double totalPayment;

	  private String cardNumber;

	  private int cvv;

	  private String expiry;

}
