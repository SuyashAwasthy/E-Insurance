package com.techlabs.app.dto;

import lombok.Data;

@Data
public class SchemeDocumentDto {
	
	private Long id;

	  private String name;
	  
	  
	  public SchemeDocumentDto(String name) {
	        this.name = name;
	    }


	public SchemeDocumentDto() {
		// TODO Auto-generated constructor stub
	}
	

}
