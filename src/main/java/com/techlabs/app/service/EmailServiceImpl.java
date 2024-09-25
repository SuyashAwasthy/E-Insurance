package com.techlabs.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
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
//	@Override
//	public boolean sendEmail(String email, String string, String emailContent) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	
	public boolean sendEmail(String recipientEmail, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("awasthysuyash@gmail.com"); // Sender's email (your Gmail)
            message.setTo(recipientEmail); // Recipient's email
            message.setSubject(subject); // Email subject
            message.setText(content); // Email body

            mailSender.send(message); // Send the email

            // Email sent successfully
            return true;
        } catch (MailException e) {
            // Handle the exception, for example log it
            System.err.println("Error sending email: " + e.getMessage());
            return false;
        }
    }

}
