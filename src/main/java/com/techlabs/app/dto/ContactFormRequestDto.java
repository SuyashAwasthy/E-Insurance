package com.techlabs.app.dto;

import lombok.Data;

@Data
public class ContactFormRequestDto {
	private Long customerId;
	private Long agentId;
	private String message;
}
