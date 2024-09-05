package com.techlabs.app.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService{

	 @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private RoleRepository roleRepository;

	    @Autowired
	    private CustomerRepository customerRepository;

	    @Autowired
	    private CityRepository cityRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    
	
	@Override
	@Transactional
	
	public void addCustomer(RegisterDto registerDto) {
		 // Check if the username or email already exists
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        // Create a new user
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Assign customer role to the user
        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Customer role not found"));
        roles.add(customerRole);
        user.setRoles(roles);

        // Save user to the repository
        userRepository.save(user);

        // Find the city using cityId
        City city = cityRepository.findById(registerDto.getCityId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));

        // Create a new customer
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFirstName(registerDto.getFirstName());
        customer.setLastName(registerDto.getLastName());
        customer.setPhoneNumber(registerDto.getPhone_number());
        customer.setDob(registerDto.getDob());
        customer.setCity(city);
        customer.setActive(true); // Set default status as active
        customer.setVerified(false); // Set default verification status

        // Save customer to the repository
        customerRepository.save(customer);
    }
	}


