package com.techlabs.app.service;

public interface EmailService {

	void sendOtpEmail(String email, String otp);

	void sendMail(long agentId, String subject, String text);

}
