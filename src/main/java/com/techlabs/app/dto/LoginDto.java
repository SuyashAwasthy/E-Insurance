package com.techlabs.app.dto;

import lombok.Data;


public class LoginDto {
	private String usernameOrEmail;
	private String password;
	private String role;
	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}
	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public LoginDto(String usernameOrEmail, String password, String role) {
		super();
		this.usernameOrEmail = usernameOrEmail;
		this.password = password;
		this.role = role;
	}
	public LoginDto() {
		super();
	}
	@Override
	public String toString() {
		return "LoginDto [usernameOrEmail=" + usernameOrEmail + ", password=" + password + ", role=" + role + "]";
	}

	

}

