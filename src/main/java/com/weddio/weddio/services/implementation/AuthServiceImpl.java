package com.weddio.weddio.services.implementation;

import com.weddio.weddio.dto.requests.LoginRequest;
import com.weddio.weddio.dto.requests.RegisterRequest;
import com.weddio.weddio.dto.requests.ResetPasswordRequest;
import com.weddio.weddio.dto.responses.LoginResponse;
import com.weddio.weddio.dto.responses.RegisterResponse;
import com.weddio.weddio.dto.responses.ResponseData;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	private Map<String, Object> createPayload(Accounts account) {
		Map<String, Object> claims = new LinkedHashMap<> ();
		claims.put("id", account.getId());
		claims.put("username", account.getUsername ());
		return claims;
	}

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

	private void clearTokenFromCookie(HttpServletResponse response) {
		ResponseCookie accessCookie = ResponseCookie.fromClientResponse("accessToken", "")
				.maxAge(0)
				.sameSite("None")
				.secure(true)
				.path("/")
				.httpOnly(true)
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
	}

	public LoginResponse login (LoginRequest loginRequest, HttpServletResponse response) {
		try{
			Accounts account = accountRepository.findByUsername (loginRequest.getUsername()).orElseThrow (() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "Account is not found"));

			if(!passwordEncoder.matches (loginRequest.getPassword(), account.getPassword())){
				throw new ResponseStatusException (HttpStatus.UNAUTHORIZED, "Wrong password");
			}

			Map<String, Object> claims = createPayload(account);
			String accessToken = jwtUtil.generateAccessToken (claims, account);
			setTokenToCookie ("accessToken", accessToken, response);

			LoginResponse loginResponse = new LoginResponse ();
			loginResponse.setToken (accessToken);
			loginResponse.setTokenExpiration (jwtUtil.getExpirationDate (accessToken).toString ());

			return loginResponse;

		}catch (ResponseStatusException e){
			throw new ResponseStatusException (e.getStatusCode (), e.getReason ());
		}
	}

	public ResponseEntity<String> logout(String accessToken, HttpServletResponse response) {
		if (accessToken != null) {
			clearTokenFromCookie(response);
		}
		return ResponseEntity.ok("{\"message\":\"Logout success\"}");
	}

	public RegisterResponse register(RegisterRequest registerRequest) {
		try{
			Optional<Accounts> existAccount = accountRepository.findByUsername (registerRequest.getUsername ());

			if(existAccount.isPresent ()){
				throw new ResponseStatusException (HttpStatus.CONFLICT, "Account already exists");
			}

			Accounts accounts = new Accounts ();
			accounts.setUsername (registerRequest.getUsername ());
			accounts.setPassword (passwordEncoder.encode (registerRequest.getPassword ()));
			accountRepository.save (accounts);

			RegisterResponse registerResponse = new RegisterResponse ();
			registerResponse.setId (accounts.getId ());
			registerResponse.setUsername (registerRequest.getUsername ());
			registerResponse.setPassword (registerRequest.getPassword ());
			return registerResponse;
		}catch (ResponseStatusException e){
			throw new ResponseStatusException (e.getStatusCode (), e.getReason ());
		}
	}

	public ResponseEntity<ResponseData<Boolean>> resetPassword (ResetPasswordRequest resetPasswordRequest){
		try{
			if( ! Objects.equals (resetPasswordRequest.getPassword (), resetPasswordRequest.getConfirmPassword ()) ){
				throw new ResponseStatusException (HttpStatus.CONFLICT, "Passwords do not match");
			}

			Accounts accounts = accountRepository.findByUsername (resetPasswordRequest.getUserName ()).orElseThrow (() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "Account is not found"));
			accounts.setPassword (passwordEncoder.encode (resetPasswordRequest.getPassword ()));
			accountRepository.save (accounts);

			return new ResponseEntity<> (new ResponseData<> (Boolean.TRUE, "password change successfully"), HttpStatus.OK);

		}catch (ResponseStatusException e){
			throw new ResponseStatusException (e.getStatusCode (), e.getReason ());
		}
	}
}
