package com.weddio.weddio.controllers;

import com.weddio.weddio.dto.requests.LoginRequest;
import com.weddio.weddio.dto.requests.RegisterRequest;
import com.weddio.weddio.dto.responses.LoginResponse;
import com.weddio.weddio.dto.responses.RegisterResponse;
import com.weddio.weddio.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
		return authService.login (loginRequest, httpServletResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@CookieValue(name = "accessToken") String accessToken, HttpServletResponse httpServletResponse) {
		return authService.logout (accessToken, httpServletResponse);
	}

	@PostMapping("/register")
	public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
		return authService.register(registerRequest);
	}
}
