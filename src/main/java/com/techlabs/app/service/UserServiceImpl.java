package com.techlabs.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.User;
import com.techlabs.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Override
	public void updatePassword(String email, String newPassword) {
//	Optional<User> user = userRepository.findByEmail(email);
//
//	        if (user != null) {
//	            user.setPassword(passwordEncoder.encode(newPassword));
//	            userRepository.save(user);
//	        } else {
//	            throw new RuntimeException("User not found.");
//	        }
		  User user = userRepository.findByEmail(email)
		            .orElseThrow(() -> new RuntimeException("User not found."));

		        user.setPassword(passwordEncoder.encode(newPassword));
		        userRepository.save(user);
	}

}
