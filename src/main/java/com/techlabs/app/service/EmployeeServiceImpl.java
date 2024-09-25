package com.techlabs.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.DocumentVerificationDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.SchemeDocumentDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.dto.UserResponseDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SchemeDocument;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AgentNotFoundException;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.exception.CustomerNotFoundException;
import com.techlabs.app.exception.EmployeeNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.EmployeeRepository;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.SubmittedDocumentRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    

    @Autowired
    private JavaMailSender mailSender; // For sending emails

    @Autowired
    private InsuranceSchemeRepository insuranceSchemeRepository;
    
    @Autowired
    private SubmittedDocumentRepository submittedDocumentRepository;
    

	@Override
	public String registerAgent(@Valid AgentRequestDto agentRequestDto) {
		if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}
		if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		 // Validate that the password is not null
        if (agentRequestDto.getPassword() == null || agentRequestDto.getPassword().isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Password must not be null or empty");
        }

        // Create and save User
        User user = new User();
        user.setUsername(agentRequestDto.getUsername());
        user.setEmail(agentRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));
       // userRepository.save(user);
       
        Role agentRole = roleRepository.findByName("ROLE_AGENT")
              .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_AGENT"));
      Set<Role> roles = new HashSet<>();
      roles.add(agentRole);
      user.setRoles(roles);
      User savedUser = userRepository.save(user);
        // Find and set City
        City city = cityRepository.findById(agentRequestDto.getCity_id())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found with id: " + agentRequestDto.getCity_id()));

        // Create and save Agent
        Agent agent = new Agent();
        agent.setAgentId(savedUser.getId());
        agent.setUser(savedUser);
        agent.setFirstName(agentRequestDto.getFirstName());
        agent.setLastName(agentRequestDto.getLastName());
        agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
        agent.setCity(city);
        agent.setActive(agentRequestDto.isActive());
        agent.setVerified(false); // Set isVerified to false by default
        agent.setRegistrationDate(LocalDate.now());
       
        agentRepository.save(agent);

        return "Agent registered successfully";
    }
	
	  
	  
//	    @Override 
//	    @Transactional 
//	    public void updateProfile(Long employeeId, EmployeeRequestDto employeeRequestDto) { 
//	    	 Employee employee = employeeRepository.findById(employeeId) 
//	                 .orElseThrow(() -> new EntityNotFoundException("Employee not found")); 
//	    	 if(employeeRequestDto.getFirstName()!=null)
//	             employee.setFirstName(employeeRequestDto.getFirstName()); 
//	    	 if(employeeRequestDto.getLastName()!=null)
//	             employee.setLastName(employeeRequestDto.getLastName()); 
//	    	 if (employeeRequestDto.getIsActive() != null) {
//	            employee.setActive(employeeRequestDto.getIsActive());
//	        }
//	    	 
//	    	 User user = employee.getUser(); // Assuming Employee has a User reference
//	    	 user.setEmail(employeeRequestDto.getEmail());
//
//	         if (user != null) {
//	             if (employeeRequestDto.getUsername() != null) {
//	                 user.setUsername(employeeRequestDto.getUsername());
//	             }
//	            // if (employeeRequestDto.getEmail() != null) {
////	                 user.setEmail(employeeRequestDto.getEmail());
//	           //  }
//	          
//	             
//	             // Save the updated User entity
//	             userRepository.save(user);
//	         }
//
//	         // Save the updated Employee entity
//	         employeeRepository.save(employee);
//
//
//	            // employeeRepository.save(employee); 
////	             if(employeeRequestDto.getUsername()!=null )
////	            	    user.setUsername(employeeRequestDto.getUsername());
////	            	    if(employeeRequestDto.getEmail()!=null )
////	            	    user.setEmail(employeeRequestDto.getEmail());
////	            	    if (employeeRequestDto.getPassword() != null && !employeeRequestDto.getPassword().isEmpty()) {
////	            	        user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));
////	            	    }
////
////	            	    if(employeeRequestDto.getFirstName()!=null )
////	            	    employee.setFirstName(employeeRequestDto.getFirstName());
////	            	    if(employeeRequestDto.getLastName()!=null )
////
////	            	    employee.setLastName(employeeRequestDto.getLastName());
////	            	  //  employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());
////	            	   // if(employeeRequestDto.isActive() )
////	            	    employee.setActive(employeeRequestDto.isActive());
////
////	            	    userRepository.save(user);
//	            	    //employeeRepository.save(employee);
////
////	            	    return "Employee updated successfully";
//	    }

	@Override
	@Transactional
	public void updateProfile(Long employeeId, EmployeeRequestDto employeeRequestDto) {
	    Employee employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new BankApiException(HttpStatus.NOT_FOUND, "Employee not found with id: " + employeeId));

	    User user = employee.getUser();
	  
	//  if (userRepository.existsByUsername(employeeRequestDto.getUsername())) {
//	        throw new BankApiException(HttpStatus.BAD_REQUEST, "Username already exists!");
//	    }
//	    if (userRepository.existsByEmail(employeeRequestDto.getEmail())) {
//	        throw new BankApiException(HttpStatus.BAD_REQUEST, "Email already exists!");
//	    }
	    if(employeeRequestDto.getUsername()!=null )
	    user.setUsername(employeeRequestDto.getUsername());
	    if(employeeRequestDto.getEmail()!=null )
	    user.setEmail(employeeRequestDto.getEmail());
	    if (employeeRequestDto.getPassword() != null && !employeeRequestDto.getPassword().isEmpty()) {
	        user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));
	    }

	    if(employeeRequestDto.getFirstName()!=null )
	    employee.setFirstName(employeeRequestDto.getFirstName());
	    if(employeeRequestDto.getLastName()!=null )

	    employee.setLastName(employeeRequestDto.getLastName());
	  //  employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());
	   // if(employeeRequestDto.isActive() )
//	    employee.setActive(employeeRequestDto.isActive());
	    if (employeeRequestDto.getIsActive() != null) {
	        employee.setActive(employeeRequestDto.getIsActive());
	    }


	    userRepository.save(user);
	    employeeRepository.save(employee);

	   // return "Employee updated successfully";
	}




		@Override
		@Transactional
		public void verifyCustomerDocuments(Long customerId) {
			 
			        Customer customer = customerRepository.findById(customerId) 
			            .orElseThrow(() -> new CustomerNotFoundException("Customer not found")); 
			        // Logic for verifying customer documents 
			        // For example, setting a flag or updating status 
			        customer.setVerified(true); // or another appropriate update 
			        customerRepository.save(customer); 
			
		}



		@Override
		@Transactional
		public void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto) {
			Customer customer = customerRepository.findById(customerId) 
		            .orElseThrow(() -> new CustomerNotFoundException("Customer not found")); 
		        // Update customer fields
			 if(customerRequestDto.getFirstName()!=null)
		customer.setFirstName(customerRequestDto.getFirstName()); 
			 if(customerRequestDto.getLastName()!=null)
		        customer.setLastName(customerRequestDto.getLastName()); 
		        if(customerRequestDto.getIsActive()!=null)
		        customer.setActive(customerRequestDto.getIsActive());
		       
		        customer.setDob(customerRequestDto.getDob());
		      //  if(customerRequestDto.getPhoneNumber()!=null)
		        customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
		     
		        // Set other fields as necessary 
		        customerRepository.save(customer); 

			
		}



		@Override
		@Transactional
		public void editAgentDetails(Long agentId, AgentRequestDto agentRequestDto) {
			Agent agent = agentRepository.findById(agentId) 
		            .orElseThrow(() -> new AgentNotFoundException("Agent not found")); 
		        // Update agent fields 
		        agent.setFirstName(agentRequestDto.getFirstName()); 
		        agent.setLastName(agentRequestDto.getLastName()); 
		        agent.setPhoneNumber(agentRequestDto.getPhoneNumber()); 
		        agent.setActive(agentRequestDto.isActive()); 
		         
		        // Set city from ID 
		        City city = cityRepository.findById(agentRequestDto.getCity_id()) 
		            .orElseThrow(() -> new CustomerNotFoundException("City not found")); 
		        agent.setCity(city); 
		         
		        // State handling if applicable 
		 
		        agentRepository.save(agent); 
		}



		@Override
		public String verifyCustomerById(Long customerId) {
			 Customer customer = customerRepository.findById(customerId)
		                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer with ID not found"));

			 System.out.println("Customer ID being set: " + customer.getCustomerId());
			 System.out.println("no-----------------");
			 
		        
//		        String panCard = customer.getPanCard();
//		        String aadhaarCard = customer.getAadhaarCard(); 
//
//		        
//		        if (panCard == null && panCard.isEmpty()) {
//		            throw new APIException(HttpStatus.BAD_REQUEST, "Customer cannot be verified without a PAN card");
//		        }
//		        if (aadhaarCard == null && aadhaarCard.isEmpty()) {
//		            throw new APIException(HttpStatus.BAD_REQUEST, "Customer cannot be verified without an Aadhaar card");
//		        }

		        
		        if (customer.isVerified()) {
		            throw new APIException(HttpStatus.BAD_REQUEST, "Customer is already verified");
		        }

		        
		        customer.setVerified(true);
		        customerRepository.save(customer);

		        return "Customer with PAN card "; //+ panCard + " and Aadhaar card " + aadhaarCard + " verified successfully";
		    }



		@Override
		public void deactivateEmployee(Long id) {
			 Employee employee = employeeRepository.findById(id)
			            .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
			        employee.setActive(false);
			        employeeRepository.save(employee);
			
		}



		@Override
		public AgentResponseDto findAgentById(long agentId) {
			Agent agent = agentRepository.findById(agentId)
		            .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

		    return convertAgentToAgentResponseDto(agent);  
		}

		private AgentResponseDto convertAgentToAgentResponseDto(Agent agent) {
			
			if (agent == null) {
		        throw new IllegalArgumentException("Agent must not be null");
		    }
		    AgentResponseDto agentDto = new AgentResponseDto();

		 
		    agentDto.setAgentId(agent.getAgentId());
//		    agentDto.setName(agent.getFirstName() + " " + agent.getLastName());
		    agentDto.setFirstName(agent.getFirstName());// Combined name
		    agentDto.setLastName(agent.getLastName());
		    agentDto.setPhoneNumber(agent.getPhoneNumber());
		    agentDto.setActive(agent.isActive());

		    // Convert and set the associated City entity to CityResponseDto
//		    if (agent.getCity() != null) {
//		        CityResponseDto cityDto = convertCityToCityResponseDto(agent.getCity());
//		        agentDto.setCity(cityDto);
//		    }
		////
//		    // Convert and set the associated User entity to UserResponseDto
		    if (agent.getUser() != null) {
		        UserResponseDto userDto = new UserResponseDto();
		        userDto.setId(agent.getUser().getId());
		        userDto.setUsername(agent.getUser().getUsername());
		        userDto.setEmail(agent.getUser().getEmail());
		        agentDto.setUserResponseDto(userDto);
		    }

		    // Convert and set associated lists (e.g., customers and commissions)
//		    if (agent.getCustomers() != null) {
//		        List<Customer> customers = agent.getCustomers().stream().collect(Collectors.toList());
//		        agentDto.setCustomers(customers);
//		    }

		    if (agent.getCommissions() != null) {
		        List<Commission> commissions = agent.getCommissions().stream().collect(Collectors.toList());
		        agentDto.setCommissions(commissions);
		    }

		    return agentDto;
		}



		@Override
		public void deactivateAgent(Long id) {
			 Agent agent = agentRepository.findById(id)
			            .orElseThrow(() -> new IllegalArgumentException("Invalid agent ID"));
			        agent.setActive(false);
			        agentRepository.save(agent);

			
		}



		@Override
		public PagedResponse<AgentResponseDto> getAllAgents(int page, int size, String sortBy, String direction) {
		    Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) 
		        ? Sort.by(sortBy).ascending() 
		        : Sort.by(sortBy).descending();

		    Pageable pageable = PageRequest.of(page, size, sort);

		    Page<Agent> agentsPage = agentRepository.findAll(pageable);

		    List<AgentResponseDto> agentDtos = agentsPage.getContent().stream()
		            .map(this::convertAgentToAgentResponseDto)
		            .collect(Collectors.toList());

		    return new PagedResponse<>(
		            agentDtos,
		            agentsPage.getNumber(),
		            agentsPage.getSize(),
		            agentsPage.getTotalElements(),
		            agentsPage.getTotalPages(),
		            agentsPage.isLast()
		    );
		}



		public PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Customer> customerPage = customerRepository.findAll(pageable);

	        List<CustomerResponseDto> customerResponseDtos = customerPage.getContent().stream()
	                .map(this::convertToDto)
	                .collect(Collectors.toList());

	        return new PagedResponse<>(
	                customerResponseDtos,
	                customerPage.getNumber(),
	                customerPage.getSize(),
	                customerPage.getTotalElements(),
	                customerPage.getTotalPages(),
	                customerPage.isLast()
	        );
	    }

	    private CustomerResponseDto convertToDto(Customer customer) {
	        CustomerResponseDto dto = new CustomerResponseDto();
	        BeanUtils.copyProperties(customer, dto);
	        if (customer.getCity() != null) {
	            dto.setCityName(customer.getCity().getCity_name()); // Adjust according to your City entity
	        }
//	       
	        return dto;
	    }



		@Override
		 @Transactional
		    public void verifyCustomer(long customerId) {
		        Customer customer = customerRepository.findById(customerId)
		                .orElseThrow(() -> new RuntimeException("Customer not found"));

		        customer.setVerified(true);
		        customerRepository.save(customer);

		       // sendVerificationEmail(customer);
		        try {
		            sendVerificationEmail(customer);
		        } catch (Exception e) {
		            System.err.println("Error sending verification email: " + e.getMessage());
		            // Optionally, handle this error or rethrow it
		        }
		    }



		private void sendVerificationEmail(Customer customer) {
			
			
			 SimpleMailMessage message = new SimpleMailMessage();
		        message.setTo(customer.getUser().getEmail()); // Assuming User entity has an email field
		        message.setSubject("Your account has been verified");
		        message.setText("Dear " + customer.getFirstName() + ",\n\n" +
		                "Your account has been successfully verified.\n\n" +
		                "Best regards,\n" +
		                "The Team");

		        mailSender.send(message);
		    }



		@Override
		public void changePassword(Long employeeId, ChangePasswordDto changePasswordDto) {
			 Employee employee = employeeRepository.findById(employeeId) 
			            .orElseThrow(() -> new RuntimeException("Customer not found")); 
			        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), employee.getUser().getPassword())) { 
			            throw new RuntimeException("Incorrect old password"); 
			        } 
			        employee.getUser().setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword())); 
			        employeeRepository.save(employee); 
			    
			
		}



//		@Override
//		public boolean verifyDocuments(long customerId, DocumentVerificationDto documentVerificationDto) {
//			
//			// Fetch the customer
//	        Customer customer = customerRepository.findById(customerId)
//	            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId));
//		
//		    // Fetch the required documents for the insurance scheme
//		    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(documentVerificationDto.getInsuranceSchemeId())
//		        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//		            "Sorry, we couldn't find a scheme with ID: " + documentVerificationDto.getInsuranceSchemeId()));
//
//		    Set<SchemeDocument> requiredDocuments = insuranceScheme.getSchemeDocuments();
//		    Set<String> requiredDocumentNames = requiredDocuments.stream()
//		        .map(SchemeDocument::getName)
//		        .collect(Collectors.toSet());
//
//		    // Fetch submitted documents
//		    List<SubmittedDocumentDto> submittedDocuments = documentVerificationDto.getDocuments();
//		    Set<String> submittedDocumentNames = submittedDocuments.stream()
//		        .map(SubmittedDocumentDto::getDocumentName)
//		        .collect(Collectors.toSet());
//
//		    // Verify if all required documents are submitted
//		    if (!requiredDocumentNames.equals(submittedDocumentNames)) {
//		        throw new APIException(HttpStatus.BAD_REQUEST, "Submitted documents do not match required documents.");
//		    }
//
////
////	        // Validate and update document status
//	        for (SubmittedDocumentDto submittedDoc : submittedDocuments) {
//	            String documentName = submittedDoc.getDocumentName();
//	         //   String documentContent = submittedDoc.getDocumentContent();
//
//	            // Find the corresponding required document
//	            SchemeDocument requiredDoc = requiredDocuments.stream()
//	                .filter(doc -> doc.getName().equals(documentName))
//	                .findFirst()
//	                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Unexpected document submitted: " + documentName));
//
////	            // Perform content validation
////	           
//
//	            // Update the document status to Verified
//	            updateDocumentStatus(customerId,documentName, "Verified");
//	        }
//
//	        return true;
//	    }
////
////
////
//		private void updateDocumentStatus(long customerId, String documentName, String status) {
//			 Customer customer = customerRepository.findById(customerId)
//				        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//				            "Customer not found with ID: " + customerId));
//
//				    // Find the document by name for the specific customer
//				    Optional<SubmittedDocument> optionalSubmittedDoc = submittedDocumentRepository.findByCustomerIdAndName(customerId, documentName);
//	        if (optionalSubmittedDoc.isPresent()) {
//	            SubmittedDocument submittedDoc = optionalSubmittedDoc.get();
//	            submittedDoc.setDocumentStatus(status);
//	            submittedDocumentRepository.save(submittedDoc);
//	        } else {
//	            throw new APIException(HttpStatus.NOT_FOUND, "Submitted document not found for customer ID " + customerId + ": " + documentName);
//	        }
//
//	}
}


