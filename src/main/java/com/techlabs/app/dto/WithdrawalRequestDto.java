package com.techlabs.app.dto;

import java.time.LocalDateTime;

import com.techlabs.app.entity.WithdrawalRequestStatus;
import com.techlabs.app.entity.WithdrawalRequestType;

import lombok.Data;
@Data
public class WithdrawalRequestDto {

	private Long withdrawalRequestId; // Corrected type to Long
    private Long agentId; // Add agentId to hold the associated agent's ID
    private WithdrawalRequestType requestType; // Consider using String if you don't want the enum directly
    private LocalDateTime requestDate;
    private WithdrawalRequestStatus status;
    private LocalDateTime approvedAt;

    private Double amount;
}
