package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.WithdrawalRequestDto;
import com.techlabs.app.entity.WithdrawalRequest;

public interface WithdrawalRequestService {
	void createWithdrawalRequest(double amount);
    WithdrawalRequest updateWithdrawalRequest(long withdrawalRequestId, WithdrawalRequest withdrawalRequest);
    void deleteWithdrawalRequest(long withdrawalRequestId);
    WithdrawalRequest getWithdrawalRequestById(long withdrawalRequestId);
    List<WithdrawalRequestDto> getAllWithdrawalRequests();
    List<WithdrawalRequest> getWithdrawalRequestsByStatus(String status);
 void approveWithdrawal(long withdrawalId);
 void rejectWithdrawal(long withdrawalId);
List<WithdrawalRequest> getWithdrawalRequestsByAgentId(long agentId);
}
