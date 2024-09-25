package com.techlabs.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.ContactFormRequestDto;
import com.techlabs.app.dto.ContactReplyRequestDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.ContactMessage;
import com.techlabs.app.entity.ContactReply;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.ContactMessageRepository;
import com.techlabs.app.repository.ContactReplyRepository;
import com.techlabs.app.repository.CustomerRepository;

@Service
public class ContactFormServiceImpl implements ContactFormService {

	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AgentRepository agentRepository;
	
	@Autowired
	private ContactMessageRepository contactMessageRepository;
	
	@Autowired
	private ContactReplyRepository contactReplyRepository;
	
	
	
	@Override
	public List<ContactMessage> getAllContactMessagesForAgent(Long agentId) {
		return contactMessageRepository.findByAgentId(agentId);
	}

	@Override
	public void submitReply(ContactReplyRequestDto replyRequest) {
		ContactReply contactReply = new ContactReply();
		contactReply.setContactMessageId(replyRequest.getContactMessageId());
		contactReply.setAgentId(replyRequest.getAgentId());
		contactReply.setReplyMessage(replyRequest.getReplyMessage());

		contactReplyRepository.save(contactReply);

	}

	@Override
	public List<ContactFormRequestDto> getAllQueries() {
		return contactMessageRepository.findAll().stream().map(message -> {
			ContactFormRequestDto dto = new ContactFormRequestDto();
			dto.setCustomerId(message.getCustomerId());
			dto.setAgentId(message.getAgentId());
			dto.setMessage(message.getMessage());
			return dto;
		}).collect(Collectors.toList());
	}

	public void submitForm(ContactFormRequestDto contactFormRequest) {
		// Validate customer and agent existence
				Customer customer = customerRepository.findById(contactFormRequest.getCustomerId())
						.orElseThrow(() -> new RuntimeException("Customer not found"));
				Agent agent = agentRepository.findById(contactFormRequest.getAgentId())
						.orElseThrow(() -> new RuntimeException("Agent not found"));

				// Create and save the contact message
				ContactMessage contactMessage = new ContactMessage();
				contactMessage.setCustomerId(contactFormRequest.getCustomerId());
				contactMessage.setAgentId(contactFormRequest.getAgentId());
				// Log the contact message to verify data
    System.out.println("Saving ContactMessage: " + contactMessage);
				contactMessage.setMessage(contactFormRequest.getMessage());

				contactMessageRepository.save(contactMessage);
	}
}
