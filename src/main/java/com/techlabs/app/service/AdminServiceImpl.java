package com.techlabs.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CityRequest;
import com.techlabs.app.dto.CityResponse;
import com.techlabs.app.dto.CityResponseDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.EmployeeResponseDto;
import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.StateRequest;
import com.techlabs.app.dto.StateResponse;
import com.techlabs.app.dto.StateResponseDto;
import com.techlabs.app.dto.TaxSettingRequestDto;
import com.techlabs.app.dto.UserResponseDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.State;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.TaxSetting;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AgentNotFoundException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AdministratorRepository;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.EmployeeRepository;
import com.techlabs.app.repository.InsurancePlanRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.StateRepository;
import com.techlabs.app.repository.TaxSettingRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

@Service
public class AdminServiceImpl implements AdminService {

    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private TaxSettingRepository taxSettingRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
private InsurancePlanRepository insurancePlanRepository;
@Autowired
private AdministratorRepository adminrepository;
@Autowired
private InsurancePolicyRepository insurancePolicyRepository;
@Autowired
private InsuranceSchemeRepository insuranceSchemeRepository;

@Autowired
private ClaimRepository claimRespository;


   
	public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
		EmployeeRepository employeeRepository, AgentRepository agentRepository, CityRepository cityRepository,
		TaxSettingRepository taxSettingRepository, StateRepository stateRepository,
		InsurancePlanRepository insurancePlanRepository, AdministratorRepository adminrepository,
		InsurancePolicyRepository insurancePolicyRepository, InsuranceSchemeRepository insuranceSchemeRepository,
		ClaimRepository claimRespository) {
	super();
	this.userRepository = userRepository;
	this.roleRepository = roleRepository;
	this.passwordEncoder = passwordEncoder;
	this.employeeRepository = employeeRepository;
	this.agentRepository = agentRepository;
	this.cityRepository = cityRepository;
	this.taxSettingRepository = taxSettingRepository;
	this.stateRepository = stateRepository;
	this.insurancePlanRepository = insurancePlanRepository;
	this.adminrepository = adminrepository;
	this.insurancePolicyRepository = insurancePolicyRepository;
	this.insuranceSchemeRepository = insuranceSchemeRepository;
	this.claimRespository = claimRespository;
}



	@Transactional
    @Override
    public String registerEmployee(EmployeeRequestDto employeeRequestDto) {

    	
    	if (userRepository.existsByUsername(employeeRequestDto.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }
        if (userRepository.existsByEmail(employeeRequestDto.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }
        
        
//        convertEmployeeRequestDtoToEmployee(employeeRequestDto);

        User user = new User();
        user.setUsername(employeeRequestDto.getUsername());
        user.setEmail(employeeRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));

        
        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_EMPLOYEE"));
        Set<Role> roles = new HashSet<>();
        roles.add(employeeRole);
        user.setRoles(roles);

       
        User savedUser = userRepository.save(user);

        
//        Employee employee = new Employee();
//        employee.setUser(savedUser);
//        employee.setName(employeeRequestDto.getName());
//        employee.setActive(employeeRequestDto.isActive());
//
//        employeeRepository.save(employee);
//		return "Employee registered successfully";
        Employee employee = new Employee();
		employee.setEmployeeId(savedUser.getId());
		employee.setUser(savedUser);
		employee.setFirstName(employeeRequestDto.getFirstName());
		employee.setLastName(employeeRequestDto.getLastName());
		employee.setActive(employeeRequestDto.isActive());
		
		employeeRepository.save(employee);
		return "Employee registered successfully";
	
    }



	@Transactional
	@Override
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
		agent.setActive(agentRequestDto.isActive());
		agent.setVerified(true);
		agentRepository.save(agent);
		return "Agent Registered successfully";
	}
//  @Override
//  public String createTaxSetting(TaxSettingRequestDto taxSettingRequestDto) {
//    TaxSetting taxSetting=new TaxSetting();
//    taxSetting.setTaxPercentage(taxSettingRequestDto.getTaxPercentage());
//    taxSetting.setUpdatedAt(taxSettingRequestDto.getUpdatedAt());
//    taxSettingRepository.save(taxSetting);
//    return "Tax Setting updated";
//  }

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
//    if (agent.getCity() != null) {
//        CityResponseDto cityDto = convertCityToCityResponseDto(agent.getCity());
//        agentDto.setCity(cityDto);
//    }
//
//    // Convert and set the associated User entity to UserResponseDto
    if (agent.getUser() != null) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(agent.getUser().getId());
        userDto.setUsername(agent.getUser().getUsername());
        userDto.setEmail(agent.getUser().getEmail());
        agentDto.setUserResponseDto(userDto);
    }

    // Convert and set associated lists (e.g., customers and commissions)
//    if (agent.getCustomers() != null) {
//        List<Customer> customers = agent.getCustomers().stream().collect(Collectors.toList());
//        agentDto.setCustomers(customers);
//    }

    if (agent.getCommissions() != null) {
        List<Commission> commissions = agent.getCommissions().stream().collect(Collectors.toList());
        agentDto.setCommissions(commissions);
    }

    return agentDto;
}

private CityResponseDto convertCityToCityResponseDto(City city) {
	if (city == null) {
        return null;
    }
	CityResponseDto cityDto = new CityResponseDto();
    cityDto.setId(city.getId());
    cityDto.setCityName(city.getCity_name());

    // Convert and set the associated State entity to StateResponseDto
    cityDto.setState(convertStateToStateResponseDto(city.getState()));

    return cityDto;
}

private StateResponseDto convertStateToStateResponseDto(State state) {
    StateResponseDto stateDto = new StateResponseDto();
    stateDto.setStateId(state.getStateId());
    stateDto.setName(state.getName());

    // Convert and set the associated City entities to CityResponseDto list if needed
    // Note: This is typically not necessary unless you need to return all cities of a state.
    // stateDto.setCities(state.getCities().stream()
    //         .map(this::convertCityToCityResponseDto)
    //         .collect(Collectors.toList()));

    return stateDto;
}


@Override
public PagedResponse<EmployeeResponseDto> getAllEmployees(int page, int size, String sortBy, String direction) {
    Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) 
        ? Sort.by(sortBy).ascending() 
        : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Employee> employeesPage = employeeRepository.findAll(pageable);

    List<EmployeeResponseDto> employeeDtos = employeesPage.getContent().stream()
            .map(this::convertEmployeeToEmployeeResponseDto)
            .collect(Collectors.toList());

    return new PagedResponse<>(
            employeeDtos,
            employeesPage.getNumber(),
            employeesPage.getSize(),
            employeesPage.getTotalElements(),
            employeesPage.getTotalPages(),
            employeesPage.isLast()
    );
}

private EmployeeResponseDto convertEmployeeToEmployeeResponseDto(Employee employee) {
    EmployeeResponseDto employeeDto = new EmployeeResponseDto();

    employeeDto.setEmployeeId(employee.getEmployeeId());
    employeeDto.setName(employee.getFirstName());
    employeeDto.setLastName(employee.getLastName());
    employeeDto.setActive(employee.isActive());
//Map User details to EmployeeResponseDto
    employeeDto.setUserId(employee.getUser().getId());
    employeeDto.setUsername(employee.getUser().getUsername());
    employeeDto.setEmail(employee.getUser().getEmail());
    employeeDto.setLastName(employee.getLastName());

    return employeeDto;
}



@Override
public EmployeeResponseDto findEmployeeByid(long id) {
 Employee employee = employeeRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

   return convertEmployeeToEmployeeResponseDto(employee);
}



@Override
public AgentResponseDto findAgentById(long agentId) {
 Agent agent = agentRepository.findById(agentId)
            .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

    return convertAgentToAgentResponseDto(agent);  
}




@Override
@Transactional
public String updateEmployee(Long employeeId, EmployeeRequestDto employeeRequestDto) {
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new BankApiException(HttpStatus.NOT_FOUND, "Employee not found with id: " + employeeId));

    User user = employee.getUser();
  
//  if (userRepository.existsByUsername(employeeRequestDto.getUsername())) {
//        throw new BankApiException(HttpStatus.BAD_REQUEST, "Username already exists!");
//    }
//    if (userRepository.existsByEmail(employeeRequestDto.getEmail())) {
//        throw new BankApiException(HttpStatus.BAD_REQUEST, "Email already exists!");
//    }
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
    employee.setActive(employeeRequestDto.isActive());

    userRepository.save(user);
    employeeRepository.save(employee);

    return "Employee updated successfully";
}




private List<AgentResponseDto> convertAgentToAgentResponseDto(List<Agent> allAgents) {
    List<AgentResponseDto> agentDtos = new ArrayList<>();

    for (Agent agent : allAgents) {
        AgentResponseDto agentDto = new AgentResponseDto();
        
      
       // agentDto.setAgentId(agent.getAgentId());
        agentDto.setName(agent.getFirstName());
        agentDto.setPhoneNumber(agent.getPhoneNumber());
        agentDto.setCity(agent.getCity());
        agentDto.setActive(agent.isActive());

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(agent.getUser().getId());
        userDto.setEmail(agent.getUser().getEmail());
        userDto.setUsername(agent.getUser().getUsername());
        // Password is generally not included in response DTOs for security reasons
         userDto.setPassword(agent.getUser().getPassword());
//
//        // Set the UserResponseDto in the AgentResponseDto
        agentDto.setUserResponseDto(userDto);

        // Convert and set the associated collections if needed
        // Assuming that customers and commissions are already initialized
       // agentDto.setCustomers(agent.getCustomers().stream().collect(Collectors.toList()));
        agentDto.setCommissions(agent.getCommissions().stream().collect(Collectors.toList()));

        // Add the AgentResponseDto to the list
        agentDtos.add(agentDto);
    }

    return agentDtos;
}

@Override
public String createState(StateRequest stateRequest) {
    State state = new State();
    state.setName(stateRequest.getName());
    state.setIsActive(true); // Default value
    stateRepository.save(state);
    return "State Added Successfully";
}

@Override
public PagedResponse<StateResponse> getAllStates(int page, int size, String sortBy, String direction) {
    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    PageRequest pageable = PageRequest.of(page, size, sort);
    Page<State> statePage = stateRepository.findAll(pageable);
    
    List<StateResponse> stateResponseList = statePage.getContent().stream().map(state -> {
        StateResponse response = new StateResponse();
        response.setStateId(state.getStateId());
        response.setName(state.getName());
        response.setIsActive(state.getIsActive());
        response.setCities(state.getCities().stream().map(city -> {
            CityResponse cityResponse = new CityResponse();
            cityResponse.setCityId(city.getId());
            cityResponse.setName(city.getCity_name());
            cityResponse.setIsActive(city.getIsActive());
            return cityResponse;
        }).collect(Collectors.toList()));
        return response;
    }).collect(Collectors.toList());
    
    return new PagedResponse<>(stateResponseList, statePage.getNumber(), statePage.getSize(), statePage.getTotalElements(), statePage.getTotalPages(), statePage.isLast());
}
@Override
public String deactivateStateById(long id) {
	State state = stateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found"));
    if (state.getIsActive()) {
        state.setIsActive(false);
        stateRepository.save(state);
    } else {
        throw new IllegalStateException("State is already deactivated");
    }
    return "State deactivated successfully";
}

@Override
public String activateStateById(long id) {
	
    
    State state = stateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found"));
    if (!state.getIsActive()) {
        state.setIsActive(true);
        stateRepository.save(state);
    } else {
        throw new IllegalStateException("State is already activated");
    }
    return "State activated successfully";


}

@Override
public String createCity(CityRequest cityRequest) {
	 try {
         State state = stateRepository.findById(cityRequest.getState_id())
                 .orElseThrow(() -> new IllegalArgumentException("Invalid state ID"));
         
         City city = new City();
         city.setCity_name(cityRequest.getName());
         city.setState(state);
         city.setIsActive(true);
         cityRepository.save(city);
         return "City Added Successfully";
     } catch (Exception e) {
         throw new RuntimeException("Error creating city: " + e.getMessage());
     }
}

@Override
public String deactivateCity(long id) {
	City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
    if (city.getIsActive()) {
        city.setIsActive(false);
        cityRepository.save(city);
    }
    return "City deactivated successfully";
}

@Override
public CityResponse getCityById(long id) {
	 City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
     CityResponse cityResponse = new CityResponse();
     cityResponse.setCityId(city.getId());
     cityResponse.setName(city.getCity_name());
     cityResponse.setIsActive(city.getIsActive());
     return cityResponse;
}

@Override
public String activateCity(long id) {
	 City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
     if (!city.getIsActive()) {
         city.setIsActive(true);
         cityRepository.save(city);
     }
     return "City activated successfully";
}

@Override
public PagedResponse<CityResponse> getAllCities(int page, int size, String sortBy, String direction) {
	   Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
       PageRequest pageable = PageRequest.of(page, size, sort);
       Page<City> cityPage = cityRepository.findAll(pageable);
       
       List<CityResponse> cityResponseList = cityPage.getContent().stream().map(city -> {
           CityResponse response = new CityResponse();
           response.setCityId(city.getId());
           response.setName(city.getCity_name());
           response.setIsActive(city.getIsActive());
           return response;
       }).collect(Collectors.toList());
       
       return new PagedResponse<>(cityResponseList, cityPage.getNumber(), cityPage.getSize(), cityPage.getTotalElements(), cityPage.getTotalPages(), cityPage.isLast());
   }




@Override
public String createTaxSetting(TaxSettingRequestDto taxSettingRequestDto) {
	// TODO Auto-generated method stub
	return null;
}




@Override
      @Transactional
      public String updateAgent(Long agentId, AgentRequestDto agentRequestDto) {
	
	if (agentId == null) {
        throw new BankApiException(HttpStatus.BAD_REQUEST, "Agent ID must not be null");
    }
    if (agentRequestDto == null) {
        throw new BankApiException(HttpStatus.BAD_REQUEST, "Agent request DTO must not be null");
    }
    if (agentRequestDto.getCity_id() == null) {
        throw new BankApiException(HttpStatus.BAD_REQUEST, "City ID must not be null");
    }
          
          Agent agent = agentRepository.findById(agentId)
                  .orElseThrow(() -> new BankApiException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

          
          User user = agent.getUser();
          if (user == null) {
              throw new BankApiException(HttpStatus.BAD_REQUEST, "Associated user not found for agent with id: " + agentId);
          }

         
//          if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
//              throw new BankApiException(HttpStatus.BAD_REQUEST, "Username already exists!");
//          }
//          if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
//              throw new BankApiException(HttpStatus.BAD_REQUEST, "Email already exists!");
//          }

          if(agentRequestDto.getUsername()!=null)
          user.setUsername(agentRequestDto.getUsername());
          if(agentRequestDto.getEmail()!=null)
          user.setEmail(agentRequestDto.getEmail());
          if (agentRequestDto.getPassword() != null && !agentRequestDto.getPassword().isEmpty()) {
              user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));
          }

         
          City city = cityRepository.findById(agentRequestDto.getCity_id())
                  .orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "City not found with id: " + agentRequestDto.getCity_id()));

          if(agentRequestDto.getFirstName()!=null)
          agent.setFirstName(agentRequestDto.getFirstName());
          if(agentRequestDto.getLastName()!=null)
          agent.setLastName(agentRequestDto.getLastName());
          if(agentRequestDto.getPhoneNumber()!=null)
          agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
          if(agentRequestDto.getCity_id()!=null)
          agent.setCity(city);  
         // if(agentRequestDto.isActive()!=null)
          agent.setActive(agentRequestDto.isActive());

         
          userRepository.save(user);
          agentRepository.save(agent);

          return "Agent updated successfully";
      }


@Override
public String createInsurancePlan(InsurancePlanDTO insurancePlanDto) {
	InsurancePlan plan = new InsurancePlan();
	plan.setName(insurancePlanDto.getName());
	plan.setActive(insurancePlanDto.isActive());
	insurancePlanRepository.save(plan);
	return "Insurance Plan created successfully.";
}

@Override
public String createInsuranceScheme(InsuranceSchemeDto insuranceSchemeDto) {
	
	// Fetch the insurance plan by its ID
    InsurancePlan plan = insurancePlanRepository.findById(insuranceSchemeDto.getInsurancePlanId())
            .orElseThrow(() -> new AllExceptions.PlanNotFoundException("Insurance plan not found"));

    // Check if the insurance plan is active
    if (!plan.isActive()) {
        throw new AllExceptions.InactivePlanException("The insurance plan is not active.");
    }

	
	InsuranceScheme scheme = new InsuranceScheme();
	scheme.setInsuranceScheme(insuranceSchemeDto.getInsuranceScheme());
	scheme.setMinimumPolicyTerm(insuranceSchemeDto.getMinimumPolicyTerm());
	scheme.setMaximumPolicyTerm(insuranceSchemeDto.getMaximumPolicyTerm());
	scheme.setMinimumAge(insuranceSchemeDto.getMinimumAge());
	scheme.setMaximumAge(insuranceSchemeDto.getMaximumAge());
	scheme.setMinimumInvestmentAmount(insuranceSchemeDto.getMinimumInvestmentAmount());
	scheme.setMaximumInvestmentAmount(insuranceSchemeDto.getMaximumInvestmentAmount());
	scheme.setProfitRatio(insuranceSchemeDto.getProfitRatio());
	scheme.setSchemeImage(insuranceSchemeDto.getSchemeImage());
	scheme.setNewRegistrationCommission(insuranceSchemeDto.getNewRegistrationCommission());
	scheme.setInstallmentPaymentCommission(insuranceSchemeDto.getInstallmentPaymentCommission());
	scheme.setDescription(insuranceSchemeDto.getDescription());

	 plan = insurancePlanRepository.findById(insuranceSchemeDto.getInsurancePlanId()).orElseThrow();
	scheme.setInsurancePlan(plan);

	insuranceSchemeRepository.save(scheme);
	return "Insurance Scheme created successfully.";
}

@Override
public String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
	// Create a new InsurancePolicy entity
	InsurancePolicy policy = new InsurancePolicy();

	// Map basic fields from the DTO to the entity
	policy.setIssuedDate(insurancePolicyDto.getIssuedDate());
	policy.setMaturityDate(insurancePolicyDto.getMaturityDate());
	policy.setPremiumAmount(insurancePolicyDto.getPremiumAmount());
	policy.setPolicyStatus(insurancePolicyDto.getPolicyStatus());
	policy.setActive(insurancePolicyDto.isActive());

	// Fetch and set associated InsuranceScheme entity
	InsuranceScheme scheme = insuranceSchemeRepository.findById(insurancePolicyDto.getInsuranceSchemeId())
			.orElseThrow(() -> new RuntimeException(
					"Insurance Scheme not found for ID: " + insurancePolicyDto.getInsuranceSchemeId()));
	policy.setInsuranceScheme(scheme);

	// Fetch and set associated Agent entity
	Agent agent = agentRepository.findById(insurancePolicyDto.getAgentId())
			.orElseThrow(() -> new RuntimeException("Agent not found for ID: " + insurancePolicyDto.getAgentId()));
	policy.setAgent(agent);

	insurancePolicyRepository.save(policy);

	return "Insurance Policy created successfully.";
}




@Override
public List<InsurancePlanDTO> getAllInsurancePlans() {
//    return insurancePlanRepository.findAll().stream()
//            .map(this::convertToInsurancePlanDTO)
//            .collect(Collectors.toList());
	 List<InsurancePlan> plans = insurancePlanRepository.findAllWithSchemes();
     return plans.stream()
             .map(this::convertToInsurancePlanDTO)
             .collect(Collectors.toList());
 }


@Override
public List<InsuranceSchemeDto> getAllInsuranceSchemes() {
    return insuranceSchemeRepository.findAll().stream()
            .map(this::convertToInsuranceSchemeDto)
            .collect(Collectors.toList());
}

@Override
public List<InsurancePolicyDto> getAllInsurancePolicies() {
    return insurancePolicyRepository.findAll().stream()
            .map(this::convertToInsurancePolicyDto)
            .collect(Collectors.toList());
}

// Conversion methods
private InsurancePlanDTO convertToInsurancePlanDTO(InsurancePlan plan) {
    return new InsurancePlanDTO(
            plan.getInsurancePlanId(),
            plan.getName(),
            plan.isActive(),
            plan.getInsuranceSchemes() != null ? 
                    plan.getInsuranceSchemes().stream()
                        .map(this::convertToInsuranceSchemeDto)
                        .collect(Collectors.toList()) : new ArrayList<>()
    );
}

private InsuranceSchemeDto convertToInsuranceSchemeDto(InsuranceScheme scheme) {
	
	if (scheme == null) return null; // Check for null
    return new InsuranceSchemeDto(
    		 scheme.getInsuranceSchemeId(), // Ensure this field is correctly populated
             scheme.getInsuranceScheme(),
             scheme.getMinimumPolicyTerm(),
             scheme.getMaximumPolicyTerm(),
             scheme.getMinimumAge(),
             scheme.getMaximumAge(),
             scheme.getMinimumInvestmentAmount(),
             scheme.getMaximumInvestmentAmount(),
             scheme.getProfitRatio(),
             scheme.getSchemeImage(),
             scheme.getNewRegistrationCommission(),
             scheme.getInstallmentPaymentCommission(),
             scheme.getDescription(),
             scheme.getInsurancePlan() != null ? scheme.getInsurancePlan().getInsurancePlanId() : 0 // Check if InsurancePlan is null
     );
}

private InsurancePolicyDto convertToInsurancePolicyDto(InsurancePolicy policy) {
//    return new InsurancePolicyDto(
//            policy.getIssuedDate(),
//            policy.getMaturityDate(),
//            policy.getPremiumAmount(),
//            policy.getPolicyStatus(),
//            policy.isActive(),
//            policy.getInsuranceScheme().getInsuranceSchemeId(),
//            policy.getAgent().getAgentId()
//    );
	 List<Long> nomineeIds = policy.getNominees().stream()
             .map(Nominee::getId)  // Assuming Nominee has a getId method
             .collect(Collectors.toList());
List<Long> paymentIds = policy.getPayments().stream()
             .map(Payment::getId)  // Assuming Payment has a getId method
             .collect(Collectors.toList());
Set<Long> documentIds = policy.getDocuments().stream()
             .map(SubmittedDocument::getId)  // Assuming SubmittedDocument has a getId method
             .collect(Collectors.toSet());
List<Long> customerIds = policy.getCustomers().stream()
              .map(Customer::getCustomerId)  // Assuming Customer has a getId method
              .collect(Collectors.toList());

return new InsurancePolicyDto(
policy.getInsuranceId(),
policy.getInsuranceScheme() != null ? policy.getInsuranceScheme().getInsuranceSchemeId() : 0,
policy.getAgent() != null ? policy.getAgent().getAgentId() : 0,
policy.getClaim() != null ? policy.getClaim().getClaimId() : 0,
nomineeIds,
paymentIds,
documentIds,
customerIds,
policy.getIssuedDate(),
policy.getMaturityDate(),
policy.getPremiumAmount(),
policy.getPolicyStatus(),
policy.isActive()
);
}



@Override
@Transactional
public String updateInsurancePlan(Long planId, InsurancePlanDTO insurancePlanDto) {
	 // Check if the insurance plan exists
	InsurancePlan existingPlan = insurancePlanRepository.findById(planId)
            .orElseThrow(() -> new RuntimeException("Insurance Plan not found with id: " + planId));

    // Update only the fields present in the InsurancePlanDTO
    existingPlan.setName(insurancePlanDto.getName());
    existingPlan.setActive(insurancePlanDto.isActive());

    // Save the updated InsurancePlan entity
    insurancePlanRepository.save(existingPlan);

    return "Insurance Plan updated successfully";
}

private InsuranceScheme convertToEntity(InsuranceSchemeDto schemeDto) {
    InsuranceScheme scheme = new InsuranceScheme();
    scheme.setInsuranceSchemeId(schemeDto.getInsuranceSchemeId());
    scheme.setInsuranceScheme(schemeDto.getInsuranceScheme());
    scheme.setMinimumPolicyTerm(schemeDto.getMinimumPolicyTerm());
    scheme.setMaximumPolicyTerm(schemeDto.getMaximumPolicyTerm());
    scheme.setMinimumAge(schemeDto.getMinimumAge());
    scheme.setMaximumAge(schemeDto.getMaximumAge());
    scheme.setMinimumInvestmentAmount(schemeDto.getMinimumInvestmentAmount());
    scheme.setMaximumInvestmentAmount(schemeDto.getMaximumInvestmentAmount());
    scheme.setProfitRatio(schemeDto.getProfitRatio());
    scheme.setSchemeImage(schemeDto.getSchemeImage());
    scheme.setNewRegistrationCommission(schemeDto.getNewRegistrationCommission());
    scheme.setInstallmentPaymentCommission(schemeDto.getInstallmentPaymentCommission());
    scheme.setDescription(schemeDto.getDescription());

    // Here you would set the InsurancePlan if needed, e.g.,
    // scheme.setInsurancePlan(existingPlan);

    return scheme;
}


@Override
@Transactional
public String updateInsurancePolicy(Long policyId, InsurancePolicyDto insurancePolicyDto) {
	// Check if the insurance policy exists
	InsurancePolicy existingPolicy = insurancePolicyRepository.findById(policyId)
            .orElseThrow(() -> new RuntimeException("Insurance Policy not found with id: " + policyId));

    // Fetch associated entities
    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(insurancePolicyDto.getInsuranceSchemeId())
            .orElseThrow(() -> new RuntimeException("Insurance Scheme not found with id: " + insurancePolicyDto.getInsuranceSchemeId()));
    
    Agent agent = agentRepository.findById(insurancePolicyDto.getAgentId())
            .orElseThrow(() -> new RuntimeException("Agent not found with id: " + insurancePolicyDto.getAgentId()));
    
//    Claim claim = claimRepository.findById(insurancePolicyDto.getClaimId())
//            .orElseThrow(() -> new RuntimeException("Claim not found with id: " + insurancePolicyDto.getClaimId()));
//    
    //List<Customer> customers = customerRepository.findAllById(insurancePolicyDto.getCustomerIds());

    // Update fields
    existingPolicy.setInsuranceScheme(insuranceScheme);
    existingPolicy.setAgent(agent);
    //existingPolicy.setClaim(claim);
//    existingPolicy.setNomineeIds(insurancePolicyDto.getNomineeIds());
//    existingPolicy.setPaymentIds(insurancePolicyDto.getPaymentIds());
//    existingPolicy.setDocumentIds(insurancePolicyDto.getDocumentIds());
//    existingPolicy.setCustomerIds(insurancePolicyDto.getCustomerIds());
    existingPolicy.setIssuedDate(insurancePolicyDto.getIssuedDate());
    existingPolicy.setMaturityDate(insurancePolicyDto.getMaturityDate());
    existingPolicy.setPremiumAmount(insurancePolicyDto.getPremiumAmount());
    existingPolicy.setPolicyStatus(insurancePolicyDto.getPolicyStatus());
    existingPolicy.setActive(insurancePolicyDto.isActive());

    // Save the updated entity
    insurancePolicyRepository.save(existingPolicy);

    return "Insurance Policy updated successfully";
}



@Override
public String updateInsuranceScheme(Long schemeId, InsuranceSchemeDto insuranceSchemeDto) {
	// Fetch the existing InsuranceScheme entity
    InsuranceScheme existingScheme = insuranceSchemeRepository.findById(schemeId)
            .orElseThrow(() -> new RuntimeException("Insurance Scheme not found with id: " + schemeId));

    InsurancePlan insurancePlan = insurancePlanRepository.findById(insuranceSchemeDto.getInsurancePlanId())
            .orElseThrow(() -> new RuntimeException("Insurance Plan not found with id: " + insuranceSchemeDto.getInsurancePlanId()));
    
    // Update fields
    existingScheme.setInsuranceScheme(insuranceSchemeDto.getInsuranceScheme());
    existingScheme.setMinimumPolicyTerm(insuranceSchemeDto.getMinimumPolicyTerm());
    existingScheme.setMaximumPolicyTerm(insuranceSchemeDto.getMaximumPolicyTerm());
    existingScheme.setMinimumAge(insuranceSchemeDto.getMinimumAge());
    existingScheme.setMaximumAge(insuranceSchemeDto.getMaximumAge());
    existingScheme.setMinimumInvestmentAmount(insuranceSchemeDto.getMinimumInvestmentAmount());
    existingScheme.setMaximumInvestmentAmount(insuranceSchemeDto.getMaximumInvestmentAmount());
    existingScheme.setProfitRatio(insuranceSchemeDto.getProfitRatio());
    existingScheme.setSchemeImage(insuranceSchemeDto.getSchemeImage());
    existingScheme.setNewRegistrationCommission(insuranceSchemeDto.getNewRegistrationCommission());
    existingScheme.setInstallmentPaymentCommission(insuranceSchemeDto.getInstallmentPaymentCommission());
    existingScheme.setDescription(insuranceSchemeDto.getDescription());
   // existingScheme.setInsurancePlan(insuranceSchemeDto.getInsurancePlanId());
    existingScheme.setInsurancePlan(insurancePlan);  
    // Save the updated entity
    insuranceSchemeRepository.save(existingScheme);

    return "Insurance Scheme updated successfully";
}



@Override
@Transactional
public boolean deactivateInsurancePlan(Long id) {
    return insurancePlanRepository.findById(id)
            .map(plan -> {
                plan.setActive(false); // Deactivate the plan
                insurancePlanRepository.save(plan); // Save the updated plan
                return true; // Convert to DTO
          })
            .orElse(false); // Return null if not found
}

// Other service methods...

private InsurancePlanDTO convertToDTO(InsurancePlan insurancePlan) {
    return new InsurancePlanDTO(insurancePlan);
}



@Override
public boolean activateInsurancePlan(Long id) {
	return insurancePlanRepository.findById(id)
            .map(plan -> {
                plan.setActive(true); // Activate the plan
                insurancePlanRepository.save(plan); // Save the updated plan
                return true; // Indicate success
            })
            .orElse(false); 


}



@Override
@Transactional
public boolean activateInsurancePolicy(Long id) {
    return insurancePolicyRepository.findById(id)
            .map(policy -> {
                policy.setActive(true); // Activate the policy
                insurancePolicyRepository.save(policy); 
                return true; 
            })
            .orElse(false); 
}

@Override
@Transactional
public boolean deactivateInsurancePolicy(Long id) {
    return insurancePolicyRepository.findById(id)
            .map(policy -> {
                policy.setActive(false); // Deactivate the policy
                insurancePolicyRepository.save(policy); // Save the updated policy
                return true; // Indicate success
            })
            .orElse(false); // Indicate failure (policy not found)
}



@Override
@Transactional
public String verifyAgent(Long agentId) {
    Agent agent = agentRepository.findById(agentId)
            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

    agent.setVerified(true);
    agentRepository.save(agent);

    return "Agent verified successfully";
}



//@Override
//public String approveAgentClaim(Long claimId,ClaimResponseDto claimDto) {
//	Claim  claim=claimRespository.findById(claimId).orElseThrow(()-> new AgentNotFoundException("claim not found"));
//	if(!ClaimStatus.PENDING.name().equals(claim.getClaimedStatus())) {
//		throw new RuntimeException("claim is not in pending status");
//	}
//	if(claimDto.isClaimedStatus()) {
//		claim.setClaimedStatus(ClaimStatus.APPROVED.name());
//	}
//	else {
//		claim.setClaimedStatus(ClaimStatus.REJECT.name());
//
//	}
//	claimRespository.save(claim);
//	return "Claim ID "+claimId +" has been approved and the commision has been decued";
//}
//
@Override
public String approveAgentClaim(Long claimId, ClaimResponseDto claimDto) {
      Claim claim = claimRespository.findById(claimId)
              .orElseThrow(() -> new RuntimeException("Claim not found"));

      if (!ClaimStatus.PENDING.name().equals(claim.getClaimedStatus())) {
          throw new RuntimeException("Claim is not in pending status.");
      }

      System.out.println("Claim status is pending. Proceeding with approval/rejection.");
      System.out.println("Claimed status from DTO: " + claimDto.isClaimedStatus());
      System.out.println(claimDto);
      System.out.println(claimId);
      if (claimDto.isClaimedStatus()) {
          // Get the associated agent and deduct the commission
        
          Agent agent = claim.getAgent();
          double totalCommission = agent.getTotalCommission();
          
          System.out.println("Agent's total commission: " + totalCommission);
          System.out.println("Claim amount: " + claim.getClaimAmount());

          if (totalCommission < claim.getClaimAmount()) {
              throw new AgentNotFoundException("Insufficient total commission amount.");
          }

          totalCommission -= claim.getClaimAmount();
          agent.setTotalCommission(totalCommission);
          agentRepository.save(agent);

          claim.setClaimedStatus(ClaimStatus.APPROVED.name());
          System.out.println("Claim approved. New commission: " + totalCommission);
      }
    
      else {
          claim.setClaimedStatus(ClaimStatus.REJECT.name());
          System.out.println("Claim rejected.");
      }
      

      claimRespository.save(claim);

      return "Claim ID " + claimId + " has been " + (claimDto.isClaimedStatus() ? "approved" : "rejected") 
             + " and the commission has been " + (claimDto.isClaimedStatus() ? "deducted." : "not deducted.");
  }



@Override
@Transactional
public String approveCustomerClaim(Long claimId, ClaimResponseDto claimDto) {
    // Find the claim by ID
    Claim claim = claimRespository.findById(claimId)
            .orElseThrow(() -> new AllExceptions.UserNotFoundException("Claim not found"));

    // Check if the claim is in "PENDING" status
    if (!claim.getClaimedStatus().equals("PENDING")) {
        throw new AgentNotFoundException("Claim is not in pending status.");
    }

    // Get the associated insurance policy
    InsurancePolicy policy = claim.getPolicy();

    if (claimDto.isClaimedStatus()) {
        // If claim is approved, deduct the claim amount from the policy's total amount paid
        double claimAmount = claim.getClaimAmount();
        double totalAmountPaid = policy.getTotalAmountPaid();

        // Check if totalAmountPaid is greater than or equal to claimAmount before deducting
        if (totalAmountPaid >= claimAmount) {
            totalAmountPaid -= claimAmount;
            policy.setTotalAmountPaid(0.0);
           // policy.setTotalAmountPaid(totalAmountPaid);
        } else {
            throw new AllExceptions.InsufficientFundsException("Insufficient total amount paid for deduction.");
        }

        // Mark the claim as approved
        claim.setClaimedStatus("APPROVED");
    } else {
        // If claim is rejected, just mark it as rejected
        claim.setClaimedStatus("REJECTED");
    }

    // Save the updated claim and policy
    claimRespository.save(claim);
    insurancePolicyRepository.save(policy);  // Save the updated policy with the deducted amount

    return "Claim has been processed and the amount has been deducted from the total amount paid.";
}
}
