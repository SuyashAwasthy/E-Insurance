package com.techlabs.app.entity; 
 
import java.time.LocalDateTime; 
 
import jakarta.persistence.Column; 
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.ManyToOne; 
import jakarta.persistence.Table; 
import lombok.Data; 
 
@Entity 
@Data 
@Table(name = "commissions") 
public class Commission { 
 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long commissionId; 
 
    @ManyToOne 
    @JoinColumn(name = "agent_id", nullable = false) 
    private Agent agent; 
 
    @ManyToOne 
    @JoinColumn(name = "policy_id", nullable = false) 
    private InsurancePolicy insurancePolicy; 
 
    @Column(nullable = false) 
    private Double amount; 
 
    @Column(nullable = false) 
    private LocalDateTime date;

	



	public Commission(Long commissionId, Agent agent, InsurancePolicy insurancePolicy, Double amount,
			LocalDateTime date) {
		super();
		this.commissionId = commissionId;
		this.agent = agent;
		this.insurancePolicy = insurancePolicy;
		this.amount = amount;
		this.date = date;
	}


	public Commission() {
		
	}

	
	
	public Long getCommissionId() {
		return commissionId;
	}


	public void setCommissionId(Long commissionId) {
		this.commissionId = commissionId;
	}


	public Agent getAgent() {
		return agent;
	}


	public void setAgent(Agent agent) {
		this.agent = agent;
	}


	public InsurancePolicy getInsurancePolicy() {
		return insurancePolicy;
	}


	public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
		this.insurancePolicy = insurancePolicy;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}


	@Override
	public String toString() {
		return "Commission [commissionId=" + commissionId + ", agent=" + agent + ", insurancePolicy=" + insurancePolicy
				+ ", amount=" + amount + ", date=" + date + "]";
	}


	
	 
 
    
}