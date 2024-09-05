package com.techlabs.app.dto;

import lombok.Data;

@Data
public class CityResponse {

	private Long cityId;
    private String name;
    private Boolean isActive;

}
