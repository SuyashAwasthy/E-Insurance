package com.techlabs.app.dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private String role;
	private Long customerId;
	private Long user_id;
	 private Long cityId; // New field for city ID

	private Long agentId;

	private Boolean isActive;
	 private String firstName;
	    private String lastName;

}

