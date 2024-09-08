package com.techlabs.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
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
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.PendingVerification;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.repository.AdministratorRepository;
import com.techlabs.app.repository.AgentRepository; // Add the AgentRepository
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.CustomerRepository; // Add the CustomerRepository
import com.techlabs.app.repository.EmployeeRepository; // Add the EmployeeRepository
import com.techlabs.app.repository.PendingVerificationRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.SubmittedDocumentRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;

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
	private CityRepository cityRepository;;
	
	@Autowired 
	private SubmittedDocumentRepository submittedDocumentRepository;
	
	@Autowired
	private PendingVerificationRepository pendingVerificationRepository;
	
	

	

	public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
			AdministratorRepository adminRepository, EmployeeRepository employeeRepository,
			AgentRepository agentRepository, CustomerRepository customerRepository, CityRepository cityRepository) {
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
	}

//	@Override
//	public String login(LoginDto loginDto) {
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		return jwtTokenProvider.generateToken(authentication);
//	}

	@Override
	  public JWTAuthResponse login(LoginDto loginDto) {
	      // Authenticate the user
	      Authentication authentication = authenticationManager.authenticate(
	              new UsernamePasswordAuthenticationToken(
	                      loginDto.getUsernameOrEmail(), 
	                      loginDto.getPassword()
	              )
	      );

	      // Set the authentication in the security context
	      SecurityContextHolder.getContext().setAuthentication(authentication);

	      // Generate the JWT token
	      String token = jwtTokenProvider.generateToken(authentication);

	      // Determine if the input is an email or username
	      User user;
	      if (loginDto.getUsernameOrEmail().contains("@")) {
	          user = userRepository.findByEmail(loginDto.getUsernameOrEmail())
	                  .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
	      } else {
	          user = userRepository.findByUsername(loginDto.getUsernameOrEmail())
	                  .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
	      }

	      // Create the response DTO
	      JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
	      jwtAuthResponse.setAccessToken(token);

	      // Set the role in the response
	      for (Role role : user.getRoles()) {
	          jwtAuthResponse.setRole(role.getName());  // Assuming the user has only one role
	          break;
	      }

	      return jwtAuthResponse;
	  }	
	
	@Transactional
	@Override
	public String register(RegisterDto registerDto) {
		
		
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
	
	private void registerCustomer(User user, RegisterDto registerDto) {
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

	    // Store PAN and Aadhaar numbers temporarily
	    System.out.println("Customer ID being set: " + customer.getCustomerId());

	    PendingVerification pendingVerification = new PendingVerification();
	    pendingVerification.setCustomerId(customer.getCustomerId());
	    
	    
	    pendingVerification.setPanCard(registerDto.getPanCard());
	    pendingVerification.setAadhaarCard(registerDto.getAadhaarCard());

	    System.out.println("Saving pending verification: " + pendingVerification);
	    pendingVerificationRepository.save(pendingVerification);
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
