package com.techlabs.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.UserResponseDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AgentServiceImpl implements AgentService{

	@Autowired
    private AgentRepository agentRepository;
//
//    @Autowired
//    private PolicyRepository policyRepository;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public String registerAgent(AgentRequestDto agentRequestDto) {
		if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}
		if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		
		User user = new User();
		user.setUsername(agentRequestDto.getUsername());
		user.setEmail(agentRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));

		
		Role agentRole = roleRepository.findByName("ROLE_AGENT")
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_AGENT"));
		Set<Role> roles = new HashSet<>();
		roles.add(agentRole);
		user.setRoles(roles);

		
		User savedUser = userRepository.save(user);

		
		City city = cityRepository.findById(agentRequestDto.getCity_id())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
						"City not found with id: " + agentRequestDto.getCity_id()));

		
		Agent agent = new Agent();
		agent.setAgentId(savedUser.getId());
		agent.setUser(savedUser);
		agent.setFirstName(agentRequestDto.getFirstName());
		agent.setLastName(agentRequestDto.getLastName());
		agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
		agent.setCity(city); 
		//agent.setActive(agentRequestDto.isActive());
		agent.setActive(true);
		agent.setVerified(false);
		agentRepository.save(agent);
		return "Agent Registered successfully";
	}

	@Override
	public AgentResponseDto getAgentById(Long id) {
		Agent agent = agentRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));

	    return convertAgentToAgentResponseDto(agent);  
	}

	private AgentResponseDto convertAgentToAgentResponseDto(Agent agent) {
		if (agent == null) {
	        throw new IllegalArgumentException("Agent must not be null");
	    }
	    AgentResponseDto agentDto = new AgentResponseDto();

	 
	    agentDto.setAgentId(agent.getAgentId());
	    agentDto.setName(agent.getFirstName() + " " + agent.getLastName()); // Combined name
	    agentDto.setPhoneNumber(agent.getPhoneNumber());
	    agentDto.setActive(agent.isActive());

	    // Convert and set the associated City entity to CityResponseDto
//	    if (agent.getCity() != null) {
//	        CityResponseDto cityDto = convertCityToCityResponseDto(agent.getCity());
//	        agentDto.setCity(cityDto);
//	    }
	//
//	    // Convert and set the associated User entity to UserResponseDto
	    if (agent.getUser() != null) {
	        UserResponseDto userDto = new UserResponseDto();
	        userDto.setId(agent.getUser().getId());
	        userDto.setUsername(agent.getUser().getUsername());
	        userDto.setEmail(agent.getUser().getEmail());
	        //agentDto.setUserResponseDto(userDto);
	    }

	    // Convert and set associated lists (e.g., customers and commissions)
//	    if (agent.getCustomers() != null) {
//	        List<Customer> customers = agent.getCustomers().stream().collect(Collectors.toList());
//	        agentDto.setCustomers(customers);
//	    }

	    if (agent.getCommissions() != null) {
	        List<Commission> commissions = agent.getCommissions().stream().collect(Collectors.toList());
	        agentDto.setCommissions(commissions);
	    }

	    return agentDto;
	}

	@Override
	public AgentResponseDto updateAgentProfile(Long id, AgentRequestDto agentRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePassword(Long id, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double calculateCommission(Long agentId, Long policyId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void withdrawCommission(Long agentId, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Double> getEarningsReport(Long agentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Double> getCommissionReport(Long agentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
