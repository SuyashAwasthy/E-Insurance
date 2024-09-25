package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.entity.SubmittedDocument;

public interface SubmittedDocumentService {

	List<SubmittedDocument> getDocumentsByPolicyId(Long policyId);

}
