package com.techlabs.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentDto {
//	private int installmentNumber;
//	private String paymentMethodId;
//    private Long policyId;
//    private String paymentType;
//    private long amount;
//    private long tax;
//    private long totalPayment;
//    private LocalDate installmentDate;
//    private Double installmentAmount;
//
//   // private long installmentAmount;
//    private String paymentStatus;
//    private LocalDateTime paidDate;
//  
//    private Long id;
//    private String paymentMethodId;
//    private Long policyId;
//    private String paymentType;
//    private Double amount;
//    private Double tax;
//    private Double totalPayment;
//    private LocalDateTime paymentDate;
//
//    private String paymentStatus;
//    private int installmentNumber;
//    private LocalDate installmentDate;
//    private LocalDateTime paidDate;
//    private Double installmentAmount;
	private Long id;
	  private String paymentMethodId;
	  private Long policyId;
	  private String paymentType;
	  private Double amount;
	  private Double tax;
	  private Double totalPayment;
	  private LocalDateTime paymentDate;

	  private String paymentStatus;
	  private int installmentNumber;
	  private LocalDate installmentDate;
	  private LocalDateTime paidDate;
	  private Double installmentAmount;  

}
