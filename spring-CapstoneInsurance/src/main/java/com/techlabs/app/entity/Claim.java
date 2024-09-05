package com.techlabs.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @PositiveOrZero(message = "Amount must be a non-negative number")
    @Column(nullable = false)
    private Double claimAmount;

    @NotBlank
    @Column(nullable = false)
    private String bankName;

    @NotBlank
    @Column(nullable = false)
    private String branchName;

    @NotBlank
    @Column(nullable = false)
    private String bankAccountId;

    @NotBlank
    @Column(nullable = false)
    private String ifscCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime date = LocalDateTime.now();

    private String claimedStatus = ClaimStatus.PENDING.name();

    @OneToOne
    @JoinColumn(name = "policyId")
    private InsurancePolicy policy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agentId")
    private Agent agent;

	public long getClaimId() {
		// TODO Auto-generated method stub
		return id;
	}

	
    

}
