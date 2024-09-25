package com.techlabs.app.dto;

import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.RelationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NomineeDto {
	
	public Nominee NomineeDto(Nominee n) {
		return n;
	}
	public NomineeDto(Nominee n) {
		// TODO Auto-generated constructor stub
	}
	private String nomineeName;
	private RelationStatus relationStatus;
private Long id;
}
