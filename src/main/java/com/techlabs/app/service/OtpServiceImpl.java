package com.techlabs.app.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.OtpEntity;
import com.techlabs.app.repository.OtpRepository;

@Service
public class OtpServiceImpl implements OtpService{

	private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRATION_MINUTES = 10;

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Append a digit (0-9)
        }
        return otp.toString();
    }

    @Override
    public void saveOtp(String email, String otp) {
    	List<OtpEntity> otpEntities = otpRepository.findByEmail(email);
        if (!otpEntities.isEmpty()) {
            OtpEntity otpEntity = otpEntities.get(0); // Assuming one entry per email
            otpEntity.setOtp(otp);
            otpEntity.setCreatedAt(LocalDateTime.now());
            otpRepository.save(otpEntity);
        } else {
            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
            otpEntity.setOtp(otp);
            otpEntity.setCreatedAt(LocalDateTime.now());
            otpRepository.save(otpEntity);
        }
    }

    @Override
    public boolean validateOtp(String email, String otp) {
    	
    	
//        OtpEntity otpEntity = otpRepository.findByEmail(email);
//        if (otpEntity != null && otpEntity.getOtp().equals(otp)) {
//            // Check if the OTP is expired
//            return LocalDateTime.now().isBefore(otpEntity.getCreatedAt().plusMinutes(OTP_EXPIRATION_MINUTES));
//        }
//        return false;
    	List<OtpEntity> otpEntities = otpRepository.findByEmail(email);
        
        if (otpEntities.size() > 1) {
            // Handle multiple results
            System.err.println("Multiple OTP records found for email: " + email);
            // Optionally remove duplicates or notify the administrator
            return false;
        }
        
        OtpEntity otpEntity = otpEntities.stream().findFirst().orElse(null);
        
        if (otpEntity != null && otpEntity.getOtp().equals(otp)) {
            // Check if the OTP is expired
            return LocalDateTime.now().isBefore(otpEntity.getCreatedAt().plusMinutes(OTP_EXPIRATION_MINUTES));
        }
        return false;
    	
    }

    @Override
    public void invalidateOtp(String email) {
//        otpRepository.deleteById(otpRepository.findByEmail(email).getId());
    	 List<OtpEntity> otpEntities = otpRepository.findByEmail(email);
    	    
    	    if (otpEntities.size() > 1) {
    	        // Handle multiple results
    	        System.err.println("Multiple OTP records found for email: " + email);
    	        // Optionally remove duplicates
    	    }
    	    
    	    if (!otpEntities.isEmpty()) {
    	        otpRepository.delete(otpEntities.get(0)); // Assuming you want to delete the first one
    	    }
    }
}
