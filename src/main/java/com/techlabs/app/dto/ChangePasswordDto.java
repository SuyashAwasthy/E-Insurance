package com.techlabs.app.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
	 private String oldPassword;  // Old password to verify before updating 
	    private String newPassword;  // New password to update 

}
