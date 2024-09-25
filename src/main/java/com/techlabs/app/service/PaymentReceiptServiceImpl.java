package com.techlabs.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.PaymentReceiptDto;
import com.techlabs.app.repository.PaymentRepository;

@Service
public class PaymentReceiptServiceImpl implements PaymentReceiptService {

	@Autowired
	 private PaymentRepository paymentRepository;
	
	
	@Override
	public PaymentReceiptDto generateReceipt(PaymentReceiptDto receiptRequest) {
		receiptRequest.setReceiptNumber("REC-" + System.currentTimeMillis());
	     receiptRequest.setPaymentDate(java.time.LocalDate.now().toString());

	     return receiptRequest;
	     
	   }

}
