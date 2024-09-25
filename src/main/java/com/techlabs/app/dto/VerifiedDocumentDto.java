package com.techlabs.app.dto;

import lombok.Data;

@Data
public class VerifiedDocumentDto {
	private String documentName;  // Name of the document
    private boolean isVerified;   

}
