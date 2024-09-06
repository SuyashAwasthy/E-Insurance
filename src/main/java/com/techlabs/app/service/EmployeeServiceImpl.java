package com.techlabs.app.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.Role;
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
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;

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
    

	@Override
	public String registerAgent(@Valid AgentRequestDto agentRequestDto) {
		
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
        agentRepository.save(agent);

        return "Agent registered successfully";
    }
	
	  
	  
	    @Override 
	    @Transactional 
	    public void updateProfile(Long employeeId, EmployeeRequestDto employeeRequestDto) { 
	    	 Employee employee = employeeRepository.findById(employeeId) 
	                 .orElseThrow(() -> new EntityNotFoundException("Employee not found")); 
	    	 if(employeeRequestDto.getFirstName()!=null)
	             employee.setFirstName(employeeRequestDto.getFirstName()); 
	    	 if(employeeRequestDto.getLastName()!=null)
	             employee.setLastName(employeeRequestDto.getLastName()); 
	             employeeRepository.save(employee); 
//	             if(employeeRequestDto.getUsername()!=null )
//	            	    user.setUsername(employeeRequestDto.getUsername());
//	            	    if(employeeRequestDto.getEmail()!=null )
//	            	    user.setEmail(employeeRequestDto.getEmail());
//	            	    if (employeeRequestDto.getPassword() != null && !employeeRequestDto.getPassword().isEmpty()) {
//	            	        user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));
//	            	    }
//
//	            	    if(employeeRequestDto.getFirstName()!=null )
//	            	    employee.setFirstName(employeeRequestDto.getFirstName());
//	            	    if(employeeRequestDto.getLastName()!=null )
//
//	            	    employee.setLastName(employeeRequestDto.getLastName());
//	            	  //  employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());
//	            	   // if(employeeRequestDto.isActive() )
//	            	    employee.setActive(employeeRequestDto.isActive());
//
//	            	    userRepository.save(user);
//	            	    employeeRepository.save(employee);
//
//	            	    return "Employee updated successfully";
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
		customer.setFirstName(customerRequestDto.getFirstName()); 
		        customer.setLastName(customerRequestDto.getLastName()); 
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
	
	
	}


