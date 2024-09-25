package com.techlabs.app.service;

import com.techlabs.app.dto.PaymentReceiptDto;

public interface PaymentReceiptService {

	PaymentReceiptDto generateReceipt(PaymentReceiptDto receiptRequest);

}
