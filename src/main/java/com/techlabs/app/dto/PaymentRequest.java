package com.techlabs.app.dto;

import lombok.Data;

@Data
public class PaymentRequest {
	
	private long amount; // Amount in cents
    private String paymentMethodId; // Stripe PaymentMethod ID

  
    private Long policyId;
    private String paymentType;
    private Double tax;
    private Double totalPayment;
    
    
  
}
