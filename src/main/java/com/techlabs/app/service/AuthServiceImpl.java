package com.techlabs.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Administrator;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.DocumentStatus;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.Documentt;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.PendingVerification;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.repository.AdministratorRepository;
import com.techlabs.app.repository.AgentRepository; // Add the AgentRepository
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.CustomerRepository; // Add the CustomerRepository
import com.techlabs.app.repository.DocumenttRepository;
import com.techlabs.app.repository.EmployeeRepository; // Add the EmployeeRepository
import com.techlabs.app.repository.PendingVerificationRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.SubmittedDocumentRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AdministratorRepository adminRepository;

	@Autowired
	private EmployeeRepository employeeRepository; // Add EmployeeRepository

	@Autowired
	private AgentRepository agentRepository; // Add AgentRepository

	@Autowired
	private CustomerRepository customerRepository; // Add CustomerRepository
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired 
	private SubmittedDocumentRepository submittedDocumentRepository;
	
	@Autowired
	private PendingVerificationRepository pendingVerificationRepository;
	
	@Autowired
	private DocumenttRepository documenttRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AgentService agentService;

	
	
	

	



public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
			AdministratorRepository adminRepository, EmployeeRepository employeeRepository,
			AgentRepository agentRepository, CustomerRepository customerRepository, CityRepository cityRepository,
			SubmittedDocumentRepository submittedDocumentRepository,
			PendingVerificationRepository pendingVerificationRepository, DocumenttRepository documenttRepository,
			EmailService emailService, AgentService agentService) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.adminRepository = adminRepository;
		this.employeeRepository = employeeRepository;
		this.agentRepository = agentRepository;
		this.customerRepository = customerRepository;
		this.cityRepository = cityRepository;
		this.submittedDocumentRepository = submittedDocumentRepository;
		this.pendingVerificationRepository = pendingVerificationRepository;
		this.documenttRepository = documenttRepository;
		this.emailService = emailService;
		this.agentService = agentService;
	}

//	@Override
//	public String login(LoginDto loginDto) {
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		return jwtTokenProvider.generateToken(authentication);
//	}

//	@Override
//	  public JWTAuthResponse login(LoginDto loginDto) {
//	      // Authenticate the user
////	      Authentication authentication = authenticationManager.authenticate(
////	              new UsernamePasswordAuthenticationToken(
////	                      loginDto.getUsernameOrEmail(), 
////	                      loginDto.getPassword()
////	              )
////	      );
////
////	      // Set the authentication in the security context
////	      SecurityContextHolder.getContext().setAuthentication(authentication);
////	      String firstName = "";
////	      String lastName = "";
////	      // Generate the JWT token
////	      String token = jwtTokenProvider.generateToken(authentication);
//
//	      // Determine if the input is an email or username
//	     // User user;
//	      User user = userRepository
//	                .findUserByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail())
//	                .orElseThrow(() -> new AllExceptions.UserNotFoundException(
//	                        "User with username or email " + loginDto.getUsernameOrEmail() + " cannot be found"));
//	      
//	      // Authenticate the user
//	      Authentication authentication = authenticationManager.authenticate(
//	              new UsernamePasswordAuthenticationToken(
//	                      loginDto.getUsernameOrEmail(), 
//	                      loginDto.getPassword()
//	              )
//	      );
//
//	      // Set the authentication in the security context
//	      SecurityContextHolder.getContext().setAuthentication(authentication);
//	      String firstName = "";
//	      String lastName = "";
//	      // Generate the JWT token
//	      String token = jwtTokenProvider.generateToken(authentication);
//	      
////	      if (loginDto.getRole().equalsIgnoreCase("ADMIN")) {
////	            Administrator admin = adminRepository.findByUserDetails(user).orElseThrow(() -> new AllExceptions.UserNotFoundException(
////	                    "Admin with username or email " + loginDto.getUsernameOrEmail() + " cannot be found"));
//////	            if (!admin.getActive()) {
//////	                throw new AdminRelatedException("Admin is not active");
//////	            }
////        }
//	      
//	      if (loginDto.getRole().equalsIgnoreCase("ADMIN")) {
//	    	    // Fetch the user entity from the database
//	    	    Administrator admin = adminRepository.findByUser(user)
//	    	            .orElseThrow(() -> new AllExceptions.UserNotFoundException(
//	    	                    "Admin with username or email " + loginDto.getUsernameOrEmail() + " cannot be found"));
//	    	    
//	      }
//
//	      if (loginDto.getRole().equalsIgnoreCase("CUSTOMER")) {
//	    	    // Fetch the user entity from the database
//	    	    Customer customer = customerRepository.findByUser(user)
//	    	            .orElseThrow(() -> new AllExceptions.UserNotFoundException(
//	    	                    "Customer with username or email " + loginDto.getUsernameOrEmail() + " cannot be found"));
//	    	    
//	      }
//	      if (loginDto.getRole().equalsIgnoreCase("AGENT")) {
//	    	    // Fetch the user entity from the database
//	    	    Agent agent = agentRepository.findByUser(user)
//	    	            .orElseThrow(() -> new AllExceptions.UserNotFoundException(
//	    	                    "Agent with username or email " + loginDto.getUsernameOrEmail() + " cannot be found"));
//	    	    
//	      }
//	      
//	      if (loginDto.getRole().equalsIgnoreCase("EMPLOYEE")) {
//	    	    // Fetch the user entity from the database
//	    	    Employee employee = employeeRepository.findByUser(user)
//	    	            .orElseThrow(() -> new AllExceptions.UserNotFoundException(
//	    	                    "Employee with username or email " + loginDto.getUsernameOrEmail() + " cannot be found"));
//	    	    
//	      }
//	      
//	      
//	      
//	      
//	      if (loginDto.getUsernameOrEmail().contains("@")) {
//	          user = userRepository.findByEmail(loginDto.getUsernameOrEmail())
//	                  .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
//	      } else {
//	          user = userRepository.findByUsername(loginDto.getUsernameOrEmail())
//	                  .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
//	      }
//
//	      // Create the response DTO
//	      JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
//	      jwtAuthResponse.setAccessToken(token);
//	      
//	      boolean isActive = false; // Initialize with false
//
//
//	      // Set the role in the response
//	      for (Role role : user.getRoles()) {
//	          jwtAuthResponse.setRole(role.getName());  // Assuming the user has only one role
//	          break;
//	      }
//	      
//	      boolean isAdmin = user.getRoles().stream()
//		            .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
//	      
//	      boolean isEmployee = user.getRoles().stream()
//		            .anyMatch(role -> role.getName().equals("ROLE_EMPLOYEE"));
//	      boolean isAgent = user.getRoles().stream()
//		            .anyMatch(role -> role.getName().equals("ROLE_AGENT"));
//
//	      boolean isCustomer = user.getRoles().stream()
//	              .anyMatch(role -> role.getName().equals("ROLE_CUSTOMER"));
//
//	      // Set customerId based on role
//	      if (isAdmin || isEmployee) {
//	    	  isActive = true; 
//	    	  firstName = user.getUsername(); // Assuming firstName and lastName exist in User
//	        
//	          jwtAuthResponse.setCustomerId(null); // Set customerId to null for non-customer roles
//	          jwtAuthResponse.setCityId(null);
//	          jwtAuthResponse.setAgentId(null);
//	      } 
//	      else if (isAgent) {
////	          Agent agent = agentRepository.findByUserId(user.getId())
////	                  .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found"));
////	          jwtAuthResponse.setCustomerId(null); // Set customerId to null for agents
////	          jwtAuthResponse.setCityId(agent.getCity() != null ? agent.getCity().getId() : null); // Set cityId for agents
//	    	  Agent agent = agentService.getAgentByUserId(user.getId()); // Use the method to get the Agent
//	    	  System.out.println("-------------------------------------------------------");
//	    	  isActive = agent.isActive();
//	    	  firstName = user.getUsername(); 
//	    	  jwtAuthResponse.setAgentId(agent.getAgentId());
//	          jwtAuthResponse.setCustomerId(null); // Set customerId to null for agents
//	          jwtAuthResponse.setCityId(agent.getCity() != null ? agent.getCity().getId() : null); // Set cityId for agents
//	          System.out.println("Agent City ID: " + (agent.getCity() != null ? agent.getCity().getId() : "No City"));
//	     }
//	      else if (isCustomer) {
//	          // Find Customer entity by User ID
//	          Customer customer = customerRepository.findByUserId(user.getId())
//	                  .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found"));
//	          isActive = customer.isActive(); 
//	          firstName = user.getUsername(); 
//	          jwtAuthResponse.setCustomerId(customer.getCustomerId());
//	          jwtAuthResponse.setCityId(customer.getCity() != null ? customer.getCity().getId() : null);
//	          jwtAuthResponse.setAgentId(null);
//	         
//	      }
//	      else {
//	          jwtAuthResponse.setCustomerId(null); // Set to null if the role does not match
//	          jwtAuthResponse.setCityId(null);
//	      }
//	      jwtAuthResponse.setUser_id(user.getId());
//	      jwtAuthResponse.setFirstName(firstName);
//	      jwtAuthResponse.setIsActive(isActive);
//
//	      // If user is not active, throw an exception
//	      if (!isActive) {
//	          throw new APIException(HttpStatus.UNAUTHORIZED, "User is not active.");
//	      }
//	      return jwtAuthResponse;
//	  }	

@Override
public JWTAuthResponse login(LoginDto loginDto) {
    // Fetch the user based on username or email
    User user = userRepository
            .findUserByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail())
            .orElseThrow(() -> new AllExceptions.UserNotFoundException(
                    "User with username or email " + loginDto.getUsernameOrEmail() + " cannot be found"));

    // Authenticate the user
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(),
                    loginDto.getPassword()
            )
    );
    // Set the authentication in the security context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Generate JWT token
    String token = jwtTokenProvider.generateToken(authentication);
    
    // Create JWTAuthResponse
    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccessToken(token);
    jwtAuthResponse.setUser_id(user.getId());

    boolean isActive = false;
    String firstName = user.getUsername(); // Default username, replace later if needed

    // Safely fetch the role from LoginDto
    String role = Optional.ofNullable(loginDto.getRole())
                          .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role is required"));

    // Check role and process accordingly
    boolean isAdmin = role.equalsIgnoreCase("ADMIN");
    boolean isEmployee = role.equalsIgnoreCase("EMPLOYEE");
    boolean isAgent = role.equalsIgnoreCase("AGENT");
    boolean isCustomer = role.equalsIgnoreCase("CUSTOMER");

    // Handle admin case (no active check here)
    if (isAdmin) {
    	Administrator agent = adminRepository.findByUser(user)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Admin not found"));
        jwtAuthResponse.setCustomerId(null);
        jwtAuthResponse.setCityId(null);
        jwtAuthResponse.setAgentId(null);
        isActive = true; // Admin is always active
    } else if (isAgent) {
        // Fetch the agent entity and check if they are active
        Agent agent = agentRepository.findByUser(user)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found"));
        isActive = agent.isActive();
        jwtAuthResponse.setAgentId(agent.getAgentId());
        jwtAuthResponse.setCityId(agent.getCity() != null ? agent.getCity().getId() : null);
        jwtAuthResponse.setCustomerId(null);
    } else if (isCustomer) {
        // Fetch the customer entity and check if they are active
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found"));
        isActive = customer.isActive();
        jwtAuthResponse.setCustomerId(customer.getCustomerId());
        jwtAuthResponse.setCityId(customer.getCity() != null ? customer.getCity().getId() : null);
        jwtAuthResponse.setAgentId(null);
    } else if (isEmployee) {
        // Fetch the employee entity and check if they are active
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Employee not found"));
        isActive = employee.isActive();
        jwtAuthResponse.setCustomerId(null);
        jwtAuthResponse.setCityId(null);
        jwtAuthResponse.setAgentId(null);
    }

    // Set firstName, isActive, and role in the response
    jwtAuthResponse.setFirstName(firstName);
    jwtAuthResponse.setIsActive(isActive);
    jwtAuthResponse.setRole(role);

    // Throw error if user is not active
    if (!isActive) {
        throw new APIException(HttpStatus.UNAUTHORIZED, "User is not active.");
    }

    return jwtAuthResponse;
}

	
	@Transactional
	@Override
	public String register(RegisterDto registerDto) throws IOException {
		
		
		if (userRepository.existsByUsername(registerDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}

		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		// Create a new User entity and set basic details
		User user = new User();
		user.setUsername(registerDto.getUsername());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		// Assign roles to the User
		Set<Role> roles = new HashSet<>();
		for (String roleName : registerDto.getRoles()) {
			Role role = roleRepository.findByName(roleName)
					.orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
			if (!roleName.equals("ROLE_ADMIN") && !roleName.equals("ROLE_CUSTOMER")) {
				throw new BankApiException(HttpStatus.BAD_REQUEST, "Only Admins and Customers can self-register");
			}
			roles.add(role);
		}
		user.setRoles(roles);

		// Save the User entity
		userRepository.save(user);

		// Handle registration for Admin and Customer roles
		for (Role role : roles) {
			if (role.getName().equals("ROLE_ADMIN")) {
				registerAdministrator(user, registerDto);
			}
			 else if (role.getName().equals("ROLE_CUSTOMER")) {
				registerCustomer(user, registerDto);
			}
		}

		return "User registered successfully!";
	}

//	private void registerCustomer(User user, RegisterDto registerDto){
//		
//		System.out.println("doing registration");
//			
//			City city = cityRepository.findById(registerDto.getCityId())
//					.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));
//
//			 
//			
//			Customer customer = new Customer();
//			customer.setCustomerId(user.getId());
//			customer.setUser(user);
//			customer.setFirstName(registerDto.getFirstName());
//			customer.setLastName(registerDto.getLastName());
//		//	customer.setPhoneNumber(registerDto.getPhone_number()); 
////			customer.setPhoneNumber(registerDto.getPhone_number());
////																	
////			customer.setDob(registerDto.getDob());
//			if (registerDto.getPhone_number() == null) {
//		        throw new APIException(HttpStatus.BAD_REQUEST, "Phone number is required");
//		    }
//		    customer.setPhoneNumber(registerDto.getPhone_number());
//
//		    if (registerDto.getDob() == null) {
//		        throw new APIException(HttpStatus.BAD_REQUEST, "Date of birth is required");
//		    }
//		    
//		
//		    
//		    
//			customer.setCity(city); 
//			customer.setActive(true); 
//			customer.setVerified(false); 
//
//			
//			customerRepository.save(customer);
//		}
	
	private void registerCustomer(User user, RegisterDto registerDto) throws IOException {
	    City city = cityRepository.findById(registerDto.getCityId())
	            .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));

	    Customer customer = new Customer();
	  customer.setCustomerId(user.getId());
	    customer.setUser(user);
	    customer.setFirstName(registerDto.getFirstName());
	    customer.setLastName(registerDto.getLastName());

	    if (registerDto.getPhone_number() == null ) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Phone number is required");
	    }
	    customer.setPhoneNumber(registerDto.getPhone_number());

	    if (registerDto.getDob() == null) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Date of birth is required");
	    }
	    customer.setDob(registerDto.getDob());

	    customer.setCity(city);
	    customer.setActive(true);
	    customer.setVerified(false);

	    customer.setRegistrationDate(LocalDate.now());
	

	  customer= customerRepository.save(customer);
	    //handleDocumentUpload(customer, registerDto);

	    // Store PAN and Aadhaar numbers temporarily
//	    System.out.println("Customer ID being set: " + customer.getCustomerId());
//
//	    PendingVerification pendingVerification = new PendingVerification();
//	    pendingVerification.setCustomerId(customer.getCustomerId());
//	    
//	    
//	    pendingVerification.setPanCard(registerDto.getPanCard());
//	    pendingVerification.setAadhaarCard(registerDto.getAadhaarCard());
//
//	    System.out.println("Saving pending verification: " + pendingVerification);
//	    pendingVerificationRepository.save(pendingVerification);
	  
	// Now send the verification email
//	    String emailContent = "Dear " + customer.getFirstName() + ",\n\nThank you for registering!";
//	    boolean emailSent = emailService.sendEmail(user.getEmail(), "Welcome to our service", emailContent);
//
//	    if (emailSent) {
//	        // If email sent successfully, mark customer as verified
//	        customer.setVerified(true);
//	        customerRepository.save(customer);
//	        System.out.println("Customer verified via email: " + user.getEmail());
//	    } else {
//	        System.out.println("Email failed to send to: " + user.getEmail());
//	    }
	}

		

private void registerAdministrator(User user, RegisterDto registerDto) {
	System.out.println("doing registration");
	Administrator administrator = new Administrator();
	administrator.setAdminId(user.getId());
	administrator.setUser(user);
	administrator.setFirstName(registerDto.getFirstName());
	administrator.setLastName(registerDto.getLastName());
	if (registerDto.getPhone_number() == null) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Phone number is required");
    }
	
	administrator.setPhoneNumber(registerDto.getPhone_number()); 
	System.out.println("Phone number being processed: " + registerDto.getPhone_number());

	adminRepository.save(administrator);
	System.out.println(administrator);
}

@Override
public Boolean validateUserToken(HttpServletRequest request, String forrole) {
    final String authHeader = request.getHeader("Authorization");
    final String token = authHeader.substring(7);
   
    String username = jwtTokenProvider.getUsername(token);
    Optional<User> byUsername = userRepository.findUserByUsernameOrEmail(username, username);
    if (byUsername.isEmpty())
        return false;
    Set<Role> roles = byUsername.get().getRoles();
    for (Role role : roles) {
        //System.out.println(role.getRoleName() + "==========================================================ROLENAME" + forrole);
        if (role.getName().equalsIgnoreCase(forrole))
            return true;
    }

    return false;
}

//private void handleDocumentUpload(Customer customer, RegisterDto registerDto) throws IOException {
//    if (registerDto.getPanCard() != null && !registerDto.getPanCard().isEmpty()) {
//	    Documentt panDocument = new Documentt();
//	    panDocument.setCustomer(customer);
//	    panDocument.setDocumentName("PAN CARD");
//	    panDocument.setContent(registerDto.getPanCard().getBytes());
//	    panDocument.setVerified(false);
//	    documenttRepository.save(panDocument);
//	}
//
//	if (registerDto.getAadhaarCard() != null && !registerDto.getAadhaarCard().isEmpty()) {
//	    Documentt aadhaarDocument = new Documentt();
//	    aadhaarDocument.setCustomer(customer);
//	    aadhaarDocument.setDocumentName("AADHAR CARD");
//	    aadhaarDocument.setContent(registerDto.getAadhaarCard().getBytes());
//	    aadhaarDocument.setVerified(false);
//	    documenttRepository.save(aadhaarDocument);
//	}
//}
//public void uploadFile(MultipartFile file) throws UserException {
//    if (file.isEmpty()) {
//        throw new UserException("Please select a file to upload.");
//    }
//
//    try {
//        // Fetch the customer and employee entities
//       
//        // Create a new Documentt entity and set its fields
//        Documentt document = new Documentt();
//        document.setDocumentName(DocumentType.SOME_TYPE);  // Set appropriate document type
//        document.setVerified(false);  // Assuming it's not verified initially
//        document.setCustomer(customer);
//        document.setVerifyBy(employee);
//        document.setContent(file.getBytes());  // Save the file content as a byte array
//        
//        // Save the document to the database
//        documentRepository.save(document);
//    } catch (IOException e) {
//        throw new UserException("Could not upload the file: Error Occurred");
//    }
//}

}


//private void uploadFile(MultipartFile file, Long userId) throws IllegalStateException, IOException {
//
//	String directory_path = "src/main/java/com/techlabs/app/attachments/";
//
//	if (file.isEmpty()) {
//		throw new UserException("Please select a file to upload.");
//	}
//
//	try {
//		directory_path = directory_path + userId + "/";
//		File directory = new File(directory_path);
//		if (!directory.exists()) {
//			directory.mkdirs();
//		}
//
//		Path path = Paths.get(directory_path + file.getOriginalFilename());
//		if (Files.exists(path)) {
//			throw new UserException("File already exists: " + file.getOriginalFilename());
//		}
//		Files.write(path, file.getBytes());
//	} catch (IOException e) {
//
//		throw new UserException("Could not upload the file: Error Occurred");
//	}
//
//}
