package com.techlabs.app.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.entity.OtpEntity;
import com.techlabs.app.repository.OtpRepository;
import com.techlabs.app.service.EmailService;
import com.techlabs.app.service.OtpService;

@RestController

@RequestMapping("/E-Insurance/auth")

public class OtpController {
	
	 @Autowired

	    private OtpService otpService;

	    @Autowired

	    private EmailService emailService;
	    
	    @Autowired
	    private OtpRepository otpRepository;
	    
	    @PostMapping("/forgot-password")

	    public String forgotPassword(@RequestParam String email) {

	        // Generate the OTP

	    	String otp = otpService.generateOTP();

	        // Send the OTP to the user's email
	        emailService.sendOtpEmail(email, otp);

	        // Store the OTP in the database
	        otpService.saveOtp(email, otp);

	        return "OTP sent to your email.";
	     
	    }
	    @PostMapping("/validate-otp")
	    public String validateOtp(@RequestParam String email, @RequestParam String otp) {
//	        OtpEntity otpEntity = otpRepository.findByEmail(email);
//
//	        if (otpEntity != null && otpEntity.getOtp().equals(otp) &&
//	            LocalDateTime.now().minusMinutes(10).isBefore(otpEntity.getCreatedAt())) {
//	            otpRepository.delete(otpEntity); // OTP is used, remove it
//	            return "OTP is valid.";
//	        }
//	        return "Invalid or expired OTP.";
//	    }
	    	 if (otpService.validateOtp(email, otp)) {
	             // Invalidate the OTP after successful validation
	             otpService.invalidateOtp(email);
	             return "OTP validated successfully. You can now reset your password.";
	         } else {
	             return "Invalid OTP. Please try again.";
	         }
	     }
	    	

}
