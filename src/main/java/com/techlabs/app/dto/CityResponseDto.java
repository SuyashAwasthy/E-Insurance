package com.techlabs.app.dto;

import lombok.Data;

@Data
public class CityResponseDto {

    private Long id;
    private String cityName;
    private StateResponseDto state;

}
