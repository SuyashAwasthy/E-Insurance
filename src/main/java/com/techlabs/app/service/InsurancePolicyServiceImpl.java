package com.techlabs.app.service;

import java.awt.print.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.PolicyStatus;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.DocumentRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.util.PagedResponse;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService{

	
	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private DocumentRepository documentRepository;
	

	
	public InsurancePolicyServiceImpl(InsurancePolicyRepository insurancePolicyRepository,
			CustomerRepository customerRepository, DocumentRepository documentRepository) {
		super();
		this.insurancePolicyRepository = insurancePolicyRepository;
		this.customerRepository = customerRepository;
		this.documentRepository = documentRepository;
	}
	@Override
	public String registerPolicyForCustomer(long customerId, long policyId, long agentId) {
//		    Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
//		        InsurancePolicy policy = insurancePolicyRepository.findById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));     
//		        Agent agent = agentService.findAgentById(agentId);
//		        if (!customer.getCity().equals(agent.getCity())) {
//		        	
//		            throw new RuntimeException("Agent and customer must be in the same city.");        }
//		        policy.setAgent(agent); // Assign agent to the policy
//		        policy.getCustomers().add(customer); // Link customer to the policy        insurancePolicyRepository.save(policy);
		        return "Policy registered successfully.";
//		    }
//		}
	}
	@Override
	public InsuranceScheme getInsuranceScheme(Long insuranceSchemeId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String createPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public List<InsurancePolicy> getPoliciesByCustomerId(Long customerId) {
//		// TODO Auto-generated method stub
//		return insurancePolicyRepository.findByCustomersId(customerId);
//	}
//	

	@Transactional
	@Override
	public InsurancePolicyDto verifyPolicyDocuments(Long policyId, List<SubmittedDocumentDto> documentDtos) {
	    // Fetch the InsurancePolicy by ID, throw exception if not found
	    InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
	            .orElseThrow(() -> new AllExceptions.PolicyNotFoundException("No Policy Found With ID: " + policyId));

	    // Get the existing submitted documents from the policy
	    Set<SubmittedDocument> existingDocuments = insurancePolicy.getDocuments();

	    // Create a map of existing documents by their ID for quick lookup
	    Map<Long, SubmittedDocument> documentMap = existingDocuments.stream()
	            .collect(Collectors.toMap(SubmittedDocument::getId, document -> document));

	    // Loop through the DTOs to update existing documents or add new ones
	    for (SubmittedDocumentDto dto : documentDtos) {
	        SubmittedDocument document;

	        // Check if the document already exists in the set
	        if (dto.getId() != null && documentMap.containsKey(dto.getId())) {
	            // Update the existing document
	            document = documentMap.get(dto.getId());
	            document.setDocumentName(dto.getDocumentName());
	            document.setDocumentStatus(dto.getDocumentStatus());
	            document.setDocumentImage(dto.getDocumentImage());
	        } else {
	            // Create a new document and add it to the existing set
	            document = new SubmittedDocument();
	            document.setDocumentName(dto.getDocumentName());
	            document.setDocumentStatus(dto.getDocumentStatus());
	            document.setDocumentImage(dto.getDocumentImage());
	            existingDocuments.add(document); // Add the new document to the existing set
	        }

	        // Save the document to the repository
	        document = documentRepository.save(document);
	    }

	    // No need to replace the entire set, just keep adding new or updating existing ones

	    // Check if all documents are approved
	    boolean allApproved = existingDocuments.stream()
	            .allMatch(doc -> "APPROVED".equalsIgnoreCase(doc.getDocumentStatus()));

	    // Set the verification status based on document approvals
	    insurancePolicy.setVerified(allApproved);

	    // Update policy status based on the verification result
	    if (insurancePolicy.getVerified()) {
	        insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());
	    } else {
	        insurancePolicy.setPolicyStatus(PolicyStatus.REJECT.name());
	    }

	    // Save the updated insurance policy
	    insurancePolicy = insurancePolicyRepository.save(insurancePolicy);

	    // Convert InsurancePolicy to InsurancePolicyDto and return
	    return entityToDto(insurancePolicy);
	}
	
	// Conversion from InsurancePolicy to InsurancePolicyDto
	private InsurancePolicyDto entityToDto(InsurancePolicy insurancePolicy) {
	    InsurancePolicyDto dto = new InsurancePolicyDto();

	    // Set basic fields
	    dto.setInsuranceId(insurancePolicy.getInsuranceId());
	    dto.setInsuranceSchemeId(insurancePolicy.getInsuranceScheme() != null ? insurancePolicy.getInsuranceScheme().getInsuranceSchemeId() : null);
	    dto.setAgentId(insurancePolicy.getAgent() != null ? insurancePolicy.getAgent().getAgentId() : null);
	    dto.setClaimId(insurancePolicy.getClaim() != null ? insurancePolicy.getClaim().getClaimId() : null);

	    // Convert and set nominee IDs
	    dto.setNomineeIds(insurancePolicy.getNominees() != null ? insurancePolicy.getNominees().stream().map(Nominee::getId).collect(Collectors.toList()) : null);

	    // Convert and set payment IDs
	    dto.setPaymentIds(insurancePolicy.getPayments() != null ? insurancePolicy.getPayments().stream().map(Payment::getId).collect(Collectors.toList()) : null);

	    // Convert and set document DTOs
	    dto.setDocuments(insurancePolicy.getDocuments() != null ? insurancePolicy.getDocuments().stream().map(this::convertToSubmittedDocumentDto).collect(Collectors.toSet()) : null);

	    // Convert and set customer IDs
	    dto.setCustomerIds(insurancePolicy.getCustomers() != null ? insurancePolicy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()) : null);

	    // Set other policy-related fields
	    dto.setIssuedDate(insurancePolicy.getIssuedDate());
	    dto.setMaturityDate(insurancePolicy.getMaturityDate());
	    dto.setPremiumAmount(insurancePolicy.getPremiumAmount());
	    dto.setPolicyStatus(insurancePolicy.getPolicyStatus());
	    dto.setActive(insurancePolicy.isActive());
	    dto.setPolicyTerm(insurancePolicy.getPolicyTerm());
	    dto.setClaimAmount(insurancePolicy.getClaimAmount());

	    return dto;
	}

	// Conversion from SubmittedDocument to SubmittedDocumentDto
	private SubmittedDocumentDto convertToSubmittedDocumentDto(SubmittedDocument document) {
	    SubmittedDocumentDto dto = new SubmittedDocumentDto();
	    dto.setId(document.getId());
	    dto.setDocumentName(document.getDocumentName());
	    dto.setDocumentStatus(document.getDocumentStatus());
	    dto.setDocumentImage(document.getDocumentImage());
	    return dto;
	}
	
	@Transactional
	@Override
	public InsurancePolicyDto updateSubmittedDocuments(Long policyId, List<SubmittedDocumentDto> documentDtos) {

	    // Fetch the InsurancePolicy from the repository
	    InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
	            .orElseThrow(() -> new AllExceptions.PolicyNotFoundException("No Policy Found With ID: " + policyId));

	    // Get the existing documents
	    Set<SubmittedDocument> existingDocuments = insurancePolicy.getDocuments();

	    // Create a map of existing documents for easy lookup
	    Map<Long, SubmittedDocument> documentMap = existingDocuments.stream()
	            .collect(Collectors.toMap(SubmittedDocument::getId, document -> document));

	    // Update the existing documents or add new ones
	    Set<SubmittedDocument> updatedDocuments = new HashSet<>();
	    for (SubmittedDocumentDto dto : documentDtos) {
	        SubmittedDocument document;
	        if (dto.getId() != null && documentMap.containsKey(dto.getId())) {
	            // If the document exists, update it
	            document = documentMap.get(dto.getId());
	            document.setDocumentName(dto.getDocumentName());
	            document.setDocumentStatus(dto.getDocumentStatus());
	            document.setDocumentImage(dto.getDocumentImage());
	        } else {
	            // If it's a new document, create it
	            document = new SubmittedDocument();
	            document.setDocumentName(dto.getDocumentName());
	            document.setDocumentStatus(dto.getDocumentStatus());
	            document.setDocumentImage(dto.getDocumentImage());
	            //document.setInsurancePolicy(insurancePolicy);  // Set the parent relationship
	        }

	        document = documentRepository.save(document);  // Save the document to the database
	        updatedDocuments.add(document);  // Add to the updated set
	    }

	    // Remove documents that are not in the updated set
	    Set<SubmittedDocument> documentsToRemove = new HashSet<>(existingDocuments);
	    documentsToRemove.removeAll(updatedDocuments);
	    for (SubmittedDocument doc : documentsToRemove) {
	        insurancePolicy.getDocuments().remove(doc);  // Remove from the relationship
	        documentRepository.delete(doc);  // Physically delete the orphaned document
	    }

	    // Add the updated documents back to the insurance policy
	    existingDocuments.clear();  // Clear the existing set in place
	    existingDocuments.addAll(updatedDocuments);  // Add updated documents to the existing collection
	    
	    // Check if all documents are approved
	   insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());

	    // Save the updated insurance policy
	    insurancePolicy = insurancePolicyRepository.save(insurancePolicy);
	    

	    return entityToDto(insurancePolicy);
	}
//	@Override
//	public Optional<InsurancePolicyDto> getPoliciesByAgentId(Long agentId) {
//		 return insurancePolicyRepository.findById(agentId);
//	}
//
//	@Override
//    public List<InsurancePolicy> getPoliciesByAgentId(Long agentId) {
//		 return insurancePolicyRepository.findByAgent_AgentId(agentId);
//        // Fetch the insurance policies by agent ID
//      //  List<InsurancePolicy> policies = insurancePolicyRepository.findByAgent_AgentId(agentId);
//        
//        // Map each InsurancePolicy to InsurancePolicyDto
////        return policies.stream()
////                .map(this::mapToDto)
////                .collect(Collectors.toList());
//    }
	
	@Override
	 public List<InsurancePolicy> getPoliciesByAgentId(Long agentId) {
	        return insurancePolicyRepository.findByAgent_AgentId(agentId);
	    }

    // Helper method to map InsurancePolicy to InsurancePolicyDto
    private InsurancePolicyDto mapToDto(InsurancePolicy policy) {
    	if (policy == null) {
            return null; // Handle case where policy is null
        }

        List<CustomerDTO> customerDtos = policy.getCustomers().stream()
            .map(customer -> new CustomerDTO(customer.getCustomerId(), customer.getFirstName())) // Assuming you have a CustomerDto
            .collect(Collectors.toList());
        
        String schemeName = policy.getInsuranceScheme() != null ? policy.getInsuranceScheme().getInsuranceScheme() : null;


        return new InsurancePolicyDto(
            policy.getInsuranceId(),
            policy.getInsuranceScheme().getInsuranceSchemeId(),
            policy.getInsuranceScheme().getInsurancePlan().getName(),
            policy.getInsuranceScheme() != null ? policy.getInsuranceScheme().getInsuranceSchemeId() : null,
                    schemeName,  // Include scheme name
                    policy.getAgent() != null ? policy.getAgent().getAgentId() : null,
                    policy.getAgent() != null ? policy.getAgent().getFirstName() : null,
                    policy.getClaim() != null ? policy.getClaim().getClaimId() : null,
           policy.getNominees().stream().map(Nominee::getId).collect(Collectors.toList()),
            policy.getPayments().stream().map(Payment::getId).collect(Collectors.toList()),
//            policy.getDocuments().stream().map(SubmittedDocument::getId).collect(Collectors.toSet()),
            policy.getCustomers() != null ? policy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()) : null,
                    customerDtos,
            policy.getIssuedDate(),
            policy.getMaturityDate(),
            policy.getPremiumAmount(),
            policy.getPolicyStatus(),
            policy.isActive()
        );
    }
//    
//	
//	@Override
//	@Transactional
//	public InsurancePolicyDto verifyPolicyDocuments(Long policyId, List<SubmittedDocumentDto> documentDtos) {
//	    // Fetch the InsurancePolicy by ID, throw exception if not found
//	    InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
//	            .orElseThrow(() -> new AllExceptions.PolicyNotFoundException("No Policy Found With ID: " + policyId));
//
//	    // Get the existing submitted documents from the policy
//	    Set<SubmittedDocument> existingDocuments = insurancePolicy.getDocuments();
//
//	    // Create a map of existing documents by their ID for quick lookup
//	    Map<Long, SubmittedDocument> documentMap = existingDocuments.stream()
//	            .collect(Collectors.toMap(SubmittedDocument::getId, document -> document));
//
//	    // Update existing documents or add new ones to the set
//	    for (SubmittedDocumentDto dto : documentDtos) {
//	        SubmittedDocument document;
//	        
//	        // Check if the document already exists in the set
//	        if (dto.getId() != null && documentMap.containsKey(dto.getId())) {
//	            // Update the existing document
//	            document = documentMap.get(dto.getId());
//	            document.setDocumentName(dto.getDocumentName());
//	            document.setDocumentStatus(dto.getDocumentStatus());
//	            document.setDocumentImage(dto.getDocumentImage());
//	        } else {
//	            // Create a new document and add it to the set
//	            document = new SubmittedDocument();
//	            document.setDocumentName(dto.getDocumentName());
//	            document.setDocumentStatus(dto.getDocumentStatus());
//	            document.setDocumentImage(dto.getDocumentImage());
//	            existingDocuments.add(document); // Add the new document to the set
//	        }
//
//	        // Save the document to the repository
//	        documentRepository.save(document);
//	    }
//
//	    // After updates, the set of documents should be the same as before (modified in-place)
//	    insurancePolicy.setDocuments(existingDocuments);  // No need to replace the collection
//
//	    // Check if all documents are approved
//	    boolean allApproved = existingDocuments.stream()
//	            .allMatch(doc -> "APPROVED".equalsIgnoreCase(doc.getDocumentStatus()));
//
//	    // Set the verification status based on document approvals
//	    insurancePolicy.setVerified(allApproved);
//
//	    // Update policy status based on the verification result
//	    if (insurancePolicy.getVerified()) {
//	        insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());
//	    } else {
//	        insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());
//	    }
//
//	    // Save the updated insurance policy
//	    insurancePolicy = insurancePolicyRepository.save(insurancePolicy);
//
//	    // Return the updated DTO
//	    return entityToDto(insurancePolicy);
//	}
//
//	// Conversion from InsurancePolicy to InsurancePolicyDto
//	private InsurancePolicyDto entityToDto(InsurancePolicy insurancePolicy) {
//	    InsurancePolicyDto dto = new InsurancePolicyDto();
//	    
//	    // Set basic fields
//	    dto.setInsuranceId(insurancePolicy.getInsuranceId());
//	    dto.setInsuranceSchemeId(insurancePolicy.getInsuranceScheme() != null ? insurancePolicy.getInsuranceScheme().getInsuranceSchemeId() : null);
//	    dto.setAgentId(insurancePolicy.getAgent() != null ? insurancePolicy.getAgent().getAgentId() : null);
//	    dto.setClaimId(insurancePolicy.getClaim() != null ? insurancePolicy.getClaim().getClaimId() : null);
//	    
//	    // Convert and set nominee IDs
//	    dto.setNomineeIds(insurancePolicy.getNominees() != null ? insurancePolicy.getNominees().stream().map(Nominee::getId).collect(Collectors.toList()) : null);
//
//	    // Convert and set payment IDs
//	    dto.setPaymentIds(insurancePolicy.getPayments() != null ? insurancePolicy.getPayments().stream().map(Payment::getId).collect(Collectors.toList()) : null);
//
//	    // Convert and set document IDs
//	    dto.setDocumentIds(insurancePolicy.getDocuments() != null ? insurancePolicy.getDocuments().stream().map(SubmittedDocument::getId).collect(Collectors.toSet()) : null);
//
//	    // Convert and set customer IDs
//	    dto.setCustomerIds(insurancePolicy.getCustomers() != null ? insurancePolicy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()) : null);
//
//	    // Set other policy-related fields
//	    dto.setIssuedDate(insurancePolicy.getIssuedDate());
//	    dto.setMaturityDate(insurancePolicy.getMaturityDate());
//	    dto.setPremiumAmount(insurancePolicy.getPremiumAmount());
//	    dto.setPolicyStatus(insurancePolicy.getPolicyStatus());
//	    dto.setActive(insurancePolicy.isActive());
//	    dto.setPolicyTerm(insurancePolicy.getPolicyTerm());
//	    dto.setClaimAmount(insurancePolicy.getClaimAmount());
//
//	    return dto;
//	}
//
// 
    @Override 
    public int countTotalInsurancePolicies() { 
        return insurancePolicyRepository.countAllInsurancePolicies(); 
    }
//    @Override
//    public List<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId) {
//        return insurancePolicyRepository.findByCustomerId(customerId);
//    }
    
//    @Override
//    public List<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId) {
//        // Fetch customer by ID
//        Optional<Customer> customerOptional = customerRepository.findById(customerId);
//
//        if (customerOptional.isEmpty()) {
//            // Return an empty list if no customer is found
//            return List.of();
//        }
//
//        Customer customer = customerOptional.get();
//
//        // Get all policies linked to this customer
//        List<InsurancePolicy> policies = customer.getInsurancePolicies();
//
//        // Convert InsurancePolicy entities to InsurancePolicyDto
//        return policies.stream().map(this::convertToDto).collect(Collectors.toList());
//    }
    @Override
    public PagedResponse<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId, int page, int size) {
        // Fetch customer by ID
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        
        if (customerOptional.isEmpty()) {
            // Return an empty paged response if no customer is found
            return new PagedResponse<>(List.of(), page, size, 0, 0, true);
        }

        Customer customer = customerOptional.get();
        
        // Define pagination logic
        PageRequest pageable = PageRequest.of(page, size);
        
        // Get paginated policies for this customer
        Page<InsurancePolicy> policyPage = insurancePolicyRepository.findByCustomerId(customerId, pageable);

        // Convert InsurancePolicy entities to InsurancePolicyDto
        List<InsurancePolicyDto> policyDtos = policyPage.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

        // Build the PagedResponse
        return new PagedResponse<>(
            policyDtos, 
            policyPage.getNumber(), 
            policyPage.getSize(), 
            policyPage.getTotalElements(), 
            policyPage.getTotalPages(), 
            policyPage.isLast()
        );
    }
    // Conversion method from InsurancePolicy entity to InsurancePolicyDto
    private InsurancePolicyDto convertToDto(InsurancePolicy policy) {
        InsurancePolicyDto dto = new InsurancePolicyDto();

        dto.setInsuranceId(policy.getInsuranceId());
        dto.setInsuranceSchemeId(policy.getInsuranceScheme().getInsuranceSchemeId());
        dto.setInsuranceScheme(policy.getInsuranceScheme().getInsuranceScheme());
if(policy.getAgent()==null) {
	dto.setAgentId((long) 0.0);
}
else {
	dto.setAgentId(policy.getAgent().getAgentId());
}
//        dto.setAgentId(policy.getAgent().getAgentId());
//        dto.setAgentName(policy.getAgent().getFirstName());

        dto.setClaimId(policy.getClaim() != null ? policy.getClaim().getClaimId() : null);
        dto.setClaimAmount(policy.getClaimAmount());

        // Convert related lists and sets (nominees, payments, customers, etc.)
        dto.setNomineeIds(policy.getNominees().stream().map(nominee -> nominee.getId()).collect(Collectors.toList()));
        dto.setPaymentIds(policy.getPayments().stream().map(payment -> payment.getId()).collect(Collectors.toList()));
        dto.setDocumentIds(policy.getDocuments().stream().map(doc -> doc.getId()).collect(Collectors.toSet()));
        dto.setCustomerIds(policy.getCustomers().stream().map(customer -> customer.getCustomerId()).collect(Collectors.toList()));

        // Set other fields
        dto.setIssuedDate(policy.getIssuedDate());
        dto.setMaturityDate(policy.getMaturityDate());
        dto.setPremiumAmount(policy.getPremiumAmount());
        dto.setPolicyStatus(policy.getPolicyStatus());
        dto.setActive(policy.isActive());
        dto.setPolicyTerm(policy.getPolicyTerm());
        dto.setInstallmentPeriod(policy.getInstallmentPeriod());

        return dto;
    }
	@Override
	public List<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}
}
    
//    @Transactional
//    @Override
//    public InsurancePolicyDto updateSubmittedDocuments(Long policyId, List<SubmittedDocumentDto> documentDtos) {
//
//        InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
//                .orElseThrow(() -> new AllExceptions.PolicyNotFoundException("No Policy Found With ID: " + policyId));
//
//        Set<SubmittedDocument> existingDocuments = insurancePolicy.getDocuments();
//
//
//        Map<Long, SubmittedDocument> documentMap = existingDocuments.stream()
//                .collect(Collectors.toMap(SubmittedDocument::getId, document -> document));
//
//
//        Set<SubmittedDocument> updatedDocuments = new HashSet<>();
//        for (SubmittedDocumentDto dto : documentDtos) {
//            SubmittedDocument document;
//            if (dto.getId() != null && documentMap.containsKey(dto.getId())) {
//
//                document = documentMap.get(dto.getId());
//                document.setDocumentName(dto.getDocumentName());
//                document.setDocumentStatus(dto.getDocumentStatus());
//                document.setDocumentImage(dto.getDocumentImage());
//            } else {
//
//                document = new SubmittedDocument();
//                document.setDocumentName(dto.getDocumentName());
//                document.setDocumentStatus(dto.getDocumentStatus());
//                document.setDocumentImage(dto.getDocumentImage());
//            }
//
//            document = documentRepository.save(document);
//            updatedDocuments.add(document);
//        }
//
//
//        insurancePolicy.setDocuments(updatedDocuments);
//        insurancePolicy = insurancePolicyRepository.save(insurancePolicy);
//
//
//        return entityToDto(insurancePolicy);
//    }




