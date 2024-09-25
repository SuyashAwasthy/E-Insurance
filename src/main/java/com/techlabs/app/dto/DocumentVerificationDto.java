package com.techlabs.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class DocumentVerificationDto {
//	
//	private long insuranceSchemeId;
//    private List<SubmittedDocumentDto> documents;
	private Long policyId;
    private List<VerifiedDocumentDto> documents; // List of documents that the employee verified
}
