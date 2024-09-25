package com.techlabs.app.service;

import java.io.IOException;

import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDto;
import com.techlabs.app.dto.RegisterDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    JWTAuthResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto) throws IOException;

    Boolean validateUserToken(HttpServletRequest request, String forrole);

}
