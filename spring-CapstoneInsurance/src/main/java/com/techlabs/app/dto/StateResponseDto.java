package com.techlabs.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class StateResponseDto {

    private Long stateId;
    private String name;
    private List<CityResponseDto> cities;  // Assuming you want to include cities in the state response

    // You can include additional fields if needed
}
