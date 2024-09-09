package com.techlabs.app.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.entity.OtpEntity;
import com.techlabs.app.repository.OtpRepository;
import com.techlabs.app.service.EmailService;
import com.techlabs.app.service.OtpService;
import com.techlabs.app.service.UserService;

@RestController

@RequestMapping("/E-Insurance/auth")
@CrossOrigin(origins="http://localhost:3000")

public class OtpController {
	
	 @Autowired

	    private OtpService otpService;

	    @Autowired

	    private EmailService emailService;
	    
	    @Autowired
	    private OtpRepository otpRepository;
	    
	    @Autowired
	    private UserService userService;
	    
	    @PostMapping("/forgot-password")

	    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

	    	 if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
	             return ResponseEntity.badRequest().body("Invalid email format.");
	         }

	    	
	        // Generate the OTP

	    	String otp = otpService.generateOTP();

	        // Send the OTP to the user's email
	        emailService.sendOtpEmail(email, otp);

	        // Store the OTP in the database
	        otpService.saveOtp(email, otp);

	        return ResponseEntity.ok("OTP sent to your email.");
	     
	    }
//	    @PostMapping("/validate-otp")
//	    public String validateOtp(@RequestParam String email, @RequestParam String otp) {
////	        OtpEntity otpEntity = otpRepository.findByEmail(email);
////
////	        if (otpEntity != null && otpEntity.getOtp().equals(otp) &&
////	            LocalDateTime.now().minusMinutes(10).isBefore(otpEntity.getCreatedAt())) {
////	            otpRepository.delete(otpEntity); // OTP is used, remove it
////	            return "OTP is valid.";
////	        }
////	        return "Invalid or expired OTP.";
////	    }
//	    	 if (otpService.validateOtp(email, otp)) {
//	             // Invalidate the OTP after successful validation
//	             otpService.invalidateOtp(email);
//	             return "OTP validated successfully. You can now reset your password.";
//	         } else {
//	             return "Invalid OTP. Please try again.";
//	         }
//	     }
	    
	    @PostMapping("/validate-otp")
	    public String validateOtpAndResetPassword(
	            @RequestParam String email, 
	            @RequestParam String otp, 
	            @RequestParam String newPassword, 
	            @RequestParam String confirmPassword) {
	        
	        if (!newPassword.equals(confirmPassword)) {
	            return "Passwords do not match.";
	        }

	        if (otpService.validateOtp(email, otp)) {
	            otpService.invalidateOtp(email);
	            userService.updatePassword(email, newPassword);
	            return "Password updated successfully.";
	        } else {
	            return "Invalid OTP. Please try again.";
	        }

	    	

	    }
}
