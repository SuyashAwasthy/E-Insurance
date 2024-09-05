package com.techlabs.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class StateResponse {
	
	private Long stateId;
    private String name;
    private Boolean isActive;
    private List<CityResponse> cities; 


}
