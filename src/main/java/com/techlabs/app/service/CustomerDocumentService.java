package com.techlabs.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.Documentt;

public interface CustomerDocumentService {

	String uploadDocument(Long customerId, DocumentType documentType, MultipartFile file);

	List<Documentt> getCustomerDocuments(Long customerId);

	String verifyDocument(int documentId, Long employeeId);

	Documentt downloadDocument(int documentId);

	//void deleteDocument(int documentId);

	//void verifyDocument(Long documentId, Long employeeId);

//	void deleteDocument(int documentId);

	String verifyDocument(long documentId, Long employeeId);

}
