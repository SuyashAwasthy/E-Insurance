package com.techlabs.app.dto;

import lombok.Data;

@Data
public class PaymentReceiptDto {
	private Long paymentId;
	private Long customerId;
	private Double amount;
	private String paymentDate;
	private String receiptNumber;
	private String paymentMethod;


}
