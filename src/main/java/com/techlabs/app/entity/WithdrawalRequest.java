package com.techlabs.app.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "withdrawal_requests")
public class WithdrawalRequest {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long withdrawalRequestId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(nullable = false)
//    private String requestType;  // e.g., CUSTOMER_WITHDRAWAL, AGENT_COMMISSION_WITHDRAWAL
//
//    @Column(nullable = false)
//    private double amount;
//
//    @Column(nullable = false)
//    private LocalDateTime requestDate;
//
//    private String status;  // e.g., PENDING, APPROVED, REJECTED
//
//	public long getWithdrawalRequestId() {
//		return withdrawalRequestId;
//	}
//
//	public void setWithdrawalRequestId(long withdrawalRequestId) {
//		this.withdrawalRequestId = withdrawalRequestId;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public String getRequestType() {
//		return requestType;
//	}
//
//	public void setRequestType(String requestType) {
//		this.requestType = requestType;
//	}
//
//	public double getAmount() {
//		return amount;
//	}
//
//	public void setAmount(double amount) {
//		this.amount = amount;
//	}
//
//	public LocalDateTime getRequestDate() {
//		return requestDate;
//	}
//
//	public void setRequestDate(LocalDateTime requestDate) {
//		this.requestDate = requestDate;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long withdrawalRequestId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore 
    private User user;  // Add this field to map the user associated with the request


    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    @JsonIgnore 
    private Agent agent;

    @Column(nullable = false)
    private WithdrawalRequestType requestType;  // e.g., CUSTOMER_WITHDRAWAL, AGENT_COMMISSION_WITHDRAWAL

    @Column(nullable = false)
    private double amount;

//    @Column(nullable = false)
//    private LocalDateTime requestDate;
    
    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalRequestStatus status;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

 public long getWithdrawalRequestId() {
  return withdrawalRequestId;
 }

 public void setWithdrawalRequestId(long withdrawalRequestId) {
  this.withdrawalRequestId = withdrawalRequestId;
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

 public Agent getAgent() {
  return agent;
 }

 public void setAgent(Agent agent) {
  this.agent = agent;
 }

 public WithdrawalRequestType getRequestType() {
  return requestType;
 }

 public void setRequestType(WithdrawalRequestType requestType) {
  this.requestType = requestType;
 }

 public WithdrawalRequestStatus getStatus() {
  return status;
 }

 public void setStatus(WithdrawalRequestStatus status) {
  this.status = status;
 }

 public LocalDateTime getApprovedAt() {
  return approvedAt;
 }

 public void setApprovedAt(LocalDateTime approvedAt) {
  this.approvedAt = approvedAt;
 }

    
}
