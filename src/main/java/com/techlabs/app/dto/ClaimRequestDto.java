package com.techlabs.app.dto;
//
//import lombok.Data;
//
//@Data
//public class ClaimRequestDto {
//	private Long policyId;
//	 private String ifscCode;
//	 private String branchName;
//	 private String bankAccountId;
//	 private String bankName;
//private Double claimAmount;
//
//}
import lombok.Data;

@Data
public class ClaimRequestDto {
    private Long policyId;
    private String ifscCode;
    private String branchName;
    private String bankAccountId;
    private String bankName;
    private Double claimAmount; // New field for the claim amount
    
    

}
