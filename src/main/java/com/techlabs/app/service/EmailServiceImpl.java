package com.techlabs.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
    private JavaMailSender mailSender;
	@Override
	public void sendOtpEmail(String email, String otp) {
		 SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(email);
	        message.setSubject("Your OTP Code");
	        message.setText("Your OTP code is: " + otp);
	        mailSender.send(message);
		
	}
	@Override
	public void sendMail(long agentId, String subject, String text) {
		// TODO Auto-generated method stub
		
	}

}
