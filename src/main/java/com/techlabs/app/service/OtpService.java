package com.techlabs.app.service;

public interface OtpService {

	String generateOTP();

	void saveOtp(String email, String otp);

	boolean validateOtp(String email, String otp);

	void invalidateOtp(String email);

}
