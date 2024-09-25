package com.techlabs.app.dto;

import lombok.Data;

@Data
public class ContactReplyRequestDto {
	
	private Long contactMessageId;
    private Long agentId;
    private String replyMessage;
private long customerId;
}
