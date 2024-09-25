package com.techlabs.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InstallmentDto {
	private int installmentNumber;
    private LocalDate installmentDate;
    private double installmentAmount;
    private String paymentStatus;
    private LocalDate paidDate;

}
