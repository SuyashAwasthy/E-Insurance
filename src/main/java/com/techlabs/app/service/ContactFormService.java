package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.ContactFormRequestDto;
import com.techlabs.app.dto.ContactReplyRequestDto;
import com.techlabs.app.entity.ContactMessage;

public interface ContactFormService {

	List<ContactMessage> getAllContactMessagesForAgent(Long agentId);

	void submitReply(ContactReplyRequestDto replyRequest);

	List<ContactFormRequestDto> getAllQueries();

	void submitForm(ContactFormRequestDto contactFormRequest);

}
