package com.weddio.weddio.services.implementation;

import com.weddio.weddio.dto.requests.LoginRequest;
import com.weddio.weddio.dto.responses.LoginResponse;
import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.repositories.AccountRepository;
import com.weddio.weddio.services.interfaces.AuthService;
import com.weddio.weddio.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	private Map<String, Object> createPayload(Accounts account) {
		Map<String, Object> claims = new LinkedHashMap<> ();
		claims.put("id", account.getId());
		claims.put("username", account.getUsername ());
		return claims;
	}

	@Override
	private void setTokenToCookie(String type, String token, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from(type, token)
				.maxAge(604800)
				.sameSite("None")
				.secure(true)
				.path("/")
				.httpOnly(true)
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	@Override
	public LoginResponse login (LoginRequest loginRequest, HttpServletResponse response) {
		try{
			Accounts account = accountRepository.findByUsername (loginRequest.getUsername()).orElseThrow (() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "Account is not found"));

			if(!passwordEncoder.matches (loginRequest.getPassword(), account.getPassword())){
				throw new ResponseStatusException (HttpStatus.UNAUTHORIZED, "Wrong password");
			}

			String accessToken = jwtUtil.generateToken (createPayload (account), account.getUsername());
			String refreshToken = jwtUtil.generateRefreshToken (createPayload (account), account.getUsername());
			String expiredToken = jwtUtil.extractExpiration (accessToken).toString ();

			setTokenToCookie ("accessToken", accessToken, response);
			setTokenToCookie ("refreshToken", refreshToken, response);

			LoginResponse loginResponse = new LoginResponse (accessToken, refreshToken);
			loginResponse.setToken (accessToken);
			loginResponse.setTokenExpiration (expiredToken);

			return loginResponse;

		}catch (ResponseStatusException e){
			throw new ResponseStatusException (e.getStatusCode (), e.getReason ());
		}
	}
}
