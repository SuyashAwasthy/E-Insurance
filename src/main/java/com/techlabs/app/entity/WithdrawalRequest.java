package com.techlabs.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "withdrawal_requests")
public class WithdrawalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long withdrawalRequestId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String requestType;  // e.g., CUSTOMER_WITHDRAWAL, AGENT_COMMISSION_WITHDRAWAL

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    private String status;  // e.g., PENDING, APPROVED, REJECTED

	public long getWithdrawalRequestId() {
		return withdrawalRequestId;
	}

	public void setWithdrawalRequestId(long withdrawalRequestId) {
		this.withdrawalRequestId = withdrawalRequestId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
}
