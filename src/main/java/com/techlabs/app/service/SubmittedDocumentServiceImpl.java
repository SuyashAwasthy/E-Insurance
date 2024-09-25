package com.techlabs.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.repository.SubmittedDocumentRepository;

@Service
public class SubmittedDocumentServiceImpl implements SubmittedDocumentService {

	 @Autowired
	    private SubmittedDocumentRepository submittedDocumentRepository;

	    // Method to get documents by policy ID
	    public List<SubmittedDocument> getDocumentsByPolicyId(Long policyId) {
	        return submittedDocumentRepository.findDocumentsByPolicyId(policyId);
	    }
}
