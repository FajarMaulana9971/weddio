package com.weddio.weddio.services.interfaces;

import com.weddio.weddio.dto.requests.LoginRequest;
import com.weddio.weddio.dto.requests.RegisterRequest;
import com.weddio.weddio.dto.requests.ResetPasswordRequest;
import com.weddio.weddio.dto.responses.LoginResponse;
import com.weddio.weddio.dto.responses.RegisterResponse;
import com.weddio.weddio.dto.responses.ResponseData;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

	LoginResponse login (LoginRequest loginRequest, HttpServletResponse response);

	RegisterResponse register(RegisterRequest registerRequest);

	ResponseEntity<ResponseData<Boolean>> resetPassword (ResetPasswordRequest resetPasswordRequest);

	ResponseEntity<String> logout(String accessToken, HttpServletResponse response);
}
