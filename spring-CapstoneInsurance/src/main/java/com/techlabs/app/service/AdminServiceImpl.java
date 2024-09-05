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
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.repository.AdministratorRepository;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
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

    

	
	public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
		EmployeeRepository employeeRepository, AgentRepository agentRepository, CityRepository cityRepository,
		TaxSettingRepository taxSettingRepository, StateRepository stateRepository,
		InsurancePlanRepository insurancePlanRepository, AdministratorRepository adminrepository) {
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

//private AgentResponseDto convertAgentToAgentResponseDto(Agent agent) {
//    AgentResponseDto agentDto = new AgentResponseDto();
//    
//    agentDto.setAgentId(agent.getAgentId());
//    agentDto.setName(agent.getName());
//    agentDto.setName(agent.getLastName());
//    agentDto.setPhoneNumber(agent.getPhoneNumber());
//    agentDto.setActive(agent.isActive());
//
//    // Convert and set the associated City entity to CityResponseDto
//  //  agentDto.setCity(convertCityToCityResponseDto(agent.getCity()));
//    //agentDto.setCity(agent.getCity());
//
//    // Convert and set the associated User entity to UserResponseDto
////    UserResponseDto userDto = new UserResponseDto();
////    userDto.setId(agent.getUser().getId());
////    userDto.setUsername(agent.getUser().getUsername());
////    userDto.setEmail(agent.getUser().getEmail());
////    agentDto.setUserResponseDto(userDto);
//
//
//    return agentDto;
//}

private AgentResponseDto convertAgentToAgentResponseDto(Agent agent) {
    AgentResponseDto agentDto = new AgentResponseDto();

    // Set the simple fields
    agentDto.setAgentId(agent.getAgentId());
    agentDto.setName(agent.getFirstName() + " " + agent.getLastName()); // Combined name
    agentDto.setPhoneNumber(agent.getPhoneNumber());
    agentDto.setActive(agent.isActive());

    // Convert and set the associated City entity to CityResponseDto
//    if (agent.getCity() != null) {
//        CityResponseDto cityDto = convertCityToCityResponseDto(agent.getCity());
//        agentDto.setCity(cityDto);
//    }

    // Convert and set the associated User entity to UserResponseDto
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
    employeeDto.setActive(employee.isActive());
//Map User details to EmployeeResponseDto
    employeeDto.setUserId(employee.getUser().getId());
    employeeDto.setUsername(employee.getUser().getUsername());
    employeeDto.setEmail(employee.getUser().getEmail());

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

//@Override
//public AgentResponseDto updateAgentById(long agentId, AgentRequestDto agentRequestDto) {
//Agent existingAgent = agentRepository.findById(agentId)
//          .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));
//
//  AgentResponseDto agentDto = new AgentResponseDto();
//  agentDto.setAgentId(agentId); // Always include the ID for reference
//
//  // Update the user details if provided
//  User user = existingAgent.getUser();
//  boolean userUpdated = false;
//  
//  if (agentRequestDto.getUsername() != null) {
//      user.setUsername(agentRequestDto.getUsername());
//      userUpdated = true;
//  }
//  if (agentRequestDto.getEmail() != null) {
//      user.setEmail(agentRequestDto.getEmail());
//      userUpdated = true;
//  }
//
//  if (userUpdated) {
//      userRepository.save(user);
//
//      // Set only the updated user fields in the response DTO
//      UserResponseDto userDto = new UserResponseDto();
//      userDto.setId(user.getId());
//      if (agentRequestDto.getUsername() != null) {
//          userDto.setUsername(agentRequestDto.getUsername());
//      }
//      if (agentRequestDto.getEmail() != null) {
//          userDto.setEmail(agentRequestDto.getEmail());
//      }
//      agentDto.setUserResponseDto(userDto);
//  }
//
//  // Update the city if provided
//  if (agentRequestDto.getCity_id() != null) {
//      City city = cityRepository.findById(agentRequestDto.getCity_id())
//              .orElseThrow(() -> new RuntimeException("City not found with id: " + agentRequestDto.getCity_id()));
//      existingAgent.setCity(city);
//      CityResponseDto cityDto = convertCityToCityResponseDto(city);
//     // agentDto.setCity(cityDto);
//      //agentDto.setCity(city);
//  }
//
//  // Update the agent details if provided
//  if (agentRequestDto.getFirstName() != null) {
//      existingAgent.setFirstName(agentRequestDto.getFirstName());
//      
//      
//      agentDto.setName(agentRequestDto.getFirstName());
//  }
//  if (agentRequestDto.getLastName() != null) {
//      existingAgent.setLastName(agentRequestDto.getLastName());
//      
//      
//      agentDto.setName(agentRequestDto.getLastName());
//  }
//  if (agentRequestDto.getPhoneNumber() != null) {
//      existingAgent.setPhoneNumber(agentRequestDto.getPhoneNumber());
//      agentDto.setPhoneNumber(agentRequestDto.getPhoneNumber());
//  }
//  
//  // Set active status if provided
//  if (agentRequestDto.isActive() != existingAgent.isActive()) {
//      existingAgent.setActive(agentRequestDto.isActive());
//      agentDto.setActive(agentRequestDto.isActive());
//  }
//
//  // Save the updated agent details
//  agentRepository.save(existingAgent);
//
//
//
//return agentDto;
//}

//@Override
//public EmployeeResponseDto updateEmployeeById(long employeeId, EmployeeRequestDto employeeRequestDto) {
//Employee existingEmployee = employeeRepository.findById(employeeId)
//          .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
//
//  boolean userUpdated = false;
//  boolean employeeUpdated = false;
//
//  EmployeeResponseDto employeeDto = new EmployeeResponseDto();
//  employeeDto.setEmployeeId(employeeId); // Always include the ID for reference
//
//  // Update the user details if provided
//  User user = existingEmployee.getUser();
//  
//  if (employeeRequestDto.getUsername() != null) {
//      user.setUsername(employeeRequestDto.getUsername());
//      userUpdated = true;
//  }
//  if (employeeRequestDto.getEmail() != null) {
//      user.setEmail(employeeRequestDto.getEmail());
//      userUpdated = true;
//  }
//
//  if (userUpdated) {
//      // Save the updated user details
//      userRepository.save(user);
//
//      // Set only the updated user fields in the response DTO
//      employeeDto.setUserId(user.getId());
//      if (employeeRequestDto.getUsername() != null) {
//          employeeDto.setUsername(employeeRequestDto.getUsername());
//      }
//      if (employeeRequestDto.getEmail() != null) {
//          employeeDto.setEmail(employeeRequestDto.getEmail());
//      }
//  }
////Update the employee details if provided
//  if (employeeRequestDto.getName() != null) {
//      existingEmployee.setName(employeeRequestDto.getName());
//      employeeUpdated = true;
//      employeeDto.setName(employeeRequestDto.getName());
//  }
//  
//  // Set active status if provided
//  if (employeeRequestDto.isActive() != existingEmployee.isActive()) {
//      existingEmployee.setActive(employeeRequestDto.isActive());
//      employeeDto.setActive(employeeRequestDto.isActive());
//      employeeUpdated = true;
//  }
//
//  if (employeeUpdated || userUpdated) {
//      System.out.println("Saving employee: " + existingEmployee);  // Debugging line
//      employeeRepository.save(existingEmployee);
//      System.out.println("Employee saved: " + existingEmployee);  // Debugging line
//  }
//
//  return employeeDto;
//}

@Override
@Transactional
public String updateEmployee(Long employeeId, EmployeeRequestDto employeeRequestDto) {
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new BankApiException(HttpStatus.NOT_FOUND, "Employee not found with id: " + employeeId));

    User user = employee.getUser();
  
  if (userRepository.existsByUsername(employeeRequestDto.getUsername())) {
        throw new BankApiException(HttpStatus.BAD_REQUEST, "Username already exists!");
    }
    if (userRepository.existsByEmail(employeeRequestDto.getEmail())) {
        throw new BankApiException(HttpStatus.BAD_REQUEST, "Email already exists!");
    }

    user.setUsername(employeeRequestDto.getUsername());
    user.setEmail(employeeRequestDto.getEmail());
    if (employeeRequestDto.getPassword() != null && !employeeRequestDto.getPassword().isEmpty()) {
        user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));
    }

    employee.setFirstName(employeeRequestDto.getFirstName());

    employee.setLastName(employeeRequestDto.getLastName());
  //  employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());
    employee.setActive(employeeRequestDto.isActive());

    userRepository.save(user);
    employeeRepository.save(employee);

    return "Employee updated successfully";
}


//private List<AgentResponseDto> convertAgenttoAgentResponseDto(List<Agent> content) {
//
//	return null;
//}



//private List<AgentResponseDto> convertCustomertoCustomerDto(List<Agent> all) {
//	List<AgentResponseDto> customers = new ArrayList<>();
//
//	for (Agent i : all) {
//		
//		Agent agent=new Agent();
//		agent.setActive(i.isActive());
//		agent.setAgentId(i.getAgentId());
//		agent.setCity(i.getCity());
//		agent.setName(i.getName());
//		agent.setPhoneNumber(i.getPhoneNumber());
////		User user=new User();
//		agent.getUser().setEmail(i.getUser().getEmail());
//		agent.getUser().setUsername(i.getUser().setUsername(null));
//
//	}
//	return customers;
//}

private List<AgentResponseDto> convertAgentToAgentResponseDto(List<Agent> allAgents) {
    List<AgentResponseDto> agentDtos = new ArrayList<>();

    for (Agent agent : allAgents) {
        // Create a new AgentResponseDto object
        AgentResponseDto agentDto = new AgentResponseDto();
        
        // Set the simple fields
        agentDto.setAgentId(agent.getAgentId());
        agentDto.setName(agent.getFirstName());
        agentDto.setPhoneNumber(agent.getPhoneNumber());
        agentDto.setCity(agent.getCity());
        agentDto.setActive(agent.isActive());

        // Convert the associated User entity to a UserResponseDto
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(agent.getUser().getId());
        userDto.setEmail(agent.getUser().getEmail());
        userDto.setUsername(agent.getUser().getUsername());
        // Password is generally not included in response DTOs for security reasons
        // userDto.setPassword(agent.getUser().getPassword());

        // Set the UserResponseDto in the AgentResponseDto
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
          
          Agent agent = agentRepository.findById(agentId)
                  .orElseThrow(() -> new BankApiException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

          
          User user = agent.getUser();

         
//          if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
//              throw new BankApiException(HttpStatus.BAD_REQUEST, "Username already exists!");
//          }
//          if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
//              throw new BankApiException(HttpStatus.BAD_REQUEST, "Email already exists!");
//          }

          
          user.setUsername(agentRequestDto.getUsername());
          user.setEmail(agentRequestDto.getEmail());
          if (agentRequestDto.getPassword() != null && !agentRequestDto.getPassword().isEmpty()) {
              user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));
          }

         
          City city = cityRepository.findById(agentRequestDto.getCity_id())
                  .orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "City not found with id: " + agentRequestDto.getCity_id()));

         
          agent.setFirstName(agentRequestDto.getFirstName());
          agent.setLastName(agentRequestDto.getLastName());
          agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
          agent.setCity(city);  
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

	InsurancePlan plan = insurancePlanRepository.findById(insuranceSchemeDto.getInsurancePlanId()).orElseThrow();
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
@Transactional
public String updateInsurancePlan(Long planId, InsurancePlanDTO insurancePlanDto) {
	InsurancePlan plan = insurancePlanRepository.findById(planId).orElseThrow(
			() -> new APIException(HttpStatus.NOT_FOUND, "Insurance Plan not found for ID: " + planId));

	plan.setName(insurancePlanDto.getName());
	plan.setActive(insurancePlanDto.isActive());
	insurancePlanRepository.save(plan);

	return "Insurance Plan updated successfully.";
}


@Override
@Transactional
public String updateInsuranceScheme(Long schemeId, InsuranceSchemeDto insuranceSchemeDto) {
	InsuranceScheme scheme = insuranceSchemeRepository.findById(schemeId).orElseThrow(
			() -> new APIException(HttpStatus.NOT_FOUND, "Insurance Scheme not found for ID: " + schemeId));

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

	InsurancePlan plan = insurancePlanRepository.findById(insuranceSchemeDto.getInsurancePlanId())
			.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
					"Insurance Plan not found for ID: " + insuranceSchemeDto.getInsurancePlanId()));
	scheme.setInsurancePlan(plan);

	insuranceSchemeRepository.save(scheme);
	return "Insurance Scheme updated successfully.";
}

@Override
@Transactional
public String updateInsurancePolicy(Long policyId, InsurancePolicyDto insurancePolicyDto) {
	InsurancePolicy policy = insurancePolicyRepository.findById(policyId).orElseThrow(
			() -> new APIException(HttpStatus.NOT_FOUND, "Insurance Policy not found for ID: " + policyId));

	policy.setIssuedDate(insurancePolicyDto.getIssuedDate());
	policy.setMaturityDate(insurancePolicyDto.getMaturityDate());
	policy.setPremiumAmount(insurancePolicyDto.getPremiumAmount());
	policy.setPolicyStatus(insurancePolicyDto.getPolicyStatus());
	policy.setActive(insurancePolicyDto.isActive());

	InsuranceScheme scheme = insuranceSchemeRepository.findById(insurancePolicyDto.getInsuranceSchemeId())
			.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
					"Insurance Scheme not found for ID: " + insurancePolicyDto.getInsuranceSchemeId()));
	policy.setInsuranceScheme(scheme);

	Agent agent = agentRepository.findById(insurancePolicyDto.getAgentId())
			.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
					"Agent not found for ID: " + insurancePolicyDto.getAgentId()));
	policy.setAgent(agent);

	insurancePolicyRepository.save(policy);

	return "Insurance Policy updated successfully.";
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
public String updateInsurancePlan(Long schemeId, InsuranceSchemeDto insuranceSchemeDto) {
	// TODO Auto-generated method stub
	return null;
}
  
}
