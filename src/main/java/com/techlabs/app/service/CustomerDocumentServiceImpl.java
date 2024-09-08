//package com.techlabs.app.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.techlabs.app.entity.Customer;
//import com.techlabs.app.entity.DocumentType;
//import com.techlabs.app.entity.Documentt;
//import com.techlabs.app.entity.Employee;
//import com.techlabs.app.exception.APIException;
//import com.techlabs.app.repository.CustomerRepository;
//import com.techlabs.app.repository.DocumentRepository;
//import com.techlabs.app.repository.DocumenttRepository;
//import com.techlabs.app.repository.EmployeeRepository;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@Service
//public class CustomerDocumentServiceImpl implements CustomerDocumentService {
//
//	private final Path uploadDir = Paths.get("uploads/");
//
////    @Autowired
////    private DocumenttRepository documentRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//    
//    
//	public CustomerDocumentServiceImpl( CustomerRepository customerRepository,
//			EmployeeRepository employeeRepository) {
//		super();
//		
//		this.customerRepository = customerRepository;
//		this.employeeRepository = employeeRepository;
//	}
//	@Override
//	public String uploadDocument(Long customerId, DocumentType documentType, MultipartFile file) {
//		try {
//            if (!Files.exists(uploadDir)) {
//                Files.createDirectories(uploadDir);  // Create directories if not exist
//            }
//
//            // Fetch the customer
//            Customer customer = customerRepository.findById(customerId)
//                    .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found"));
//
//            // Save the document file
//            String fileName = documentType.name() + "_" + file.getOriginalFilename();
//            Path filePath = uploadDir.resolve(fileName);
//            Files.copy(file.getInputStream(), filePath);
//
//            // Save the document record in the database
//            Documentt document = new Documentt();
//            document.setDocumentName(documentType);
//            document.setCustomer(customer);
//            document.setContent(Files.readAllBytes(filePath));  // Save the file content in the DB
//            document.setVerified(false);  // Initially, document is not verified
//            documentRepository.save(document);
//
//            return "Document uploaded successfully.";
//
//        } catch (IOException e) {
//            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving document.");
//        }
//}
//	@Override
//	public List<Documentt> getCustomerDocuments(Long customerId) {
//		 return documentRepository.findByCustomer_CustomerId(customerId);
//	}
////	@Override
////	public void verifyDocument(int documentId, Long employeeId) {
////		   // Fetch document and employee
////        Documentt document = documentRepository.findById(documentId)
////                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Document not found"));
////
////        Employee employee = employeeRepository.findById(employeeId)
////                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Employee not found"));
////
////        // Set verified flag and assign employee
////        document.setVerified(true);
////        document.setVerifyBy(employee);
////        documentRepository.save(document);
////
////        return "Document verified successfully by employee " + employee.getFirstName() + " " + employee.getLastName();
////	}
////	@Override
////	public Documentt downloadDocument(int documentId) {
////		// TODO Auto-generated method stub
////		return null;
////	}
////	@Override
////	public void deleteDocument(int documentId) {
////		// TODO Auto-generated method stub
////		
////	}
//	@Override
//	public String verifyDocument(long documentId, Long employeeId) {
//		   Documentt document = documentRepository.findById(documentId)
//	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Document not found"));
//
//	        Employee employee = employeeRepository.findById(employeeId)
//	                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Employee not found"));
//
//	        // Set verified flag and assign employee
//	        document.setVerified(true);
//	        document.setVerifyBy(employee);
//	        documentRepository.save(document);
//
//	        //return "Document verified successfully by employee " + employee.getFirstName() + " " + employee.getLastName();
//		return "document succesfully verified";
//	}
//	@Override
//	public Documentt downloadDocument(int documentId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//}
