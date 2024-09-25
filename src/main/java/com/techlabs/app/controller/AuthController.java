package com.techlabs.app.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.StripeChargeDto;
import com.techlabs.app.service.AuthService;
//import com.techlabs.app.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/E-Insurance/auth")
@CrossOrigin(origins="http://localhost:3000")
public class AuthController {

	private AuthService authService;
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

//	@Autowired
//	StripeService stripService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// Build Login REST API
//	@PostMapping(value = { "/login", "/signin" })
//	public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
//		logger.trace("A TRACE Message");
//		logger.debug("A DEBUG Message");
//		logger.info("An INFO Message");
//		logger.warn("A WARN Message");
//		logger.error("An ERROR Message");
//		String token = authService.login(loginDto);
//		System.out.println(loginDto);
//		JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
//		jwtAuthResponse.setAccessToken(token);
//
//		return ResponseEntity.ok(jwtAuthResponse);
//	}
	
	@PostMapping("/login")
	  public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
	      logger.debug("Login attempt for user: " + loginDto.getUsernameOrEmail());
	      try {
	          // Perform login and get the JWTAuthResponse which includes token and role
	          JWTAuthResponse jwtAuthResponse = authService.login(loginDto);
	          
	          logger.info("User logged in successfully with role: {}", jwtAuthResponse.getRole());
	          return ResponseEntity.ok(jwtAuthResponse);
	      } catch (Exception e) {
	          logger.error("Login failed", e);
	          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	      }
	  }

	// Build Register REST API
	@PostMapping(value = { "/register", "/signup" })
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) throws IOException {
		System.out.println("ADmin registeratiobn");

		logger.trace("A TRACE Message" + registerDto);
		logger.debug("A DEBUG Message");
		logger.info("An INFO Message");
		logger.warn("A WARN Message");
		logger.error("An ERROR Message");
		String response = authService.register(registerDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

//	@PostMapping("/test")
//	public String testing(@RequestBody StripeChargeDto stripeCharge) {
//		stripService.charge(stripeCharge);
//		return "success";
//
//	}
	@PreAuthorize("hasAnyRole('ADMIN','AGENT','EMPLOYEE','CUSTOMER')")
	@GetMapping("/user")
    public boolean validateUserToken(@RequestParam String role,HttpServletRequest request) {
       role = "ROLE_" + role.toUpperCase();
        return authService.validateUserToken(request,role);
    }
}
