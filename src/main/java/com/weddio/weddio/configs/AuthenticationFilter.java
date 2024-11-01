package com.weddio.weddio.configs;

import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.services.interfaces.AccountService;
import com.weddio.weddio.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class AuthenticationFilter extends BasicAuthenticationFilter {
	private final JwtUtil jwtUtil;
	private final AccountService accountService;

	public AuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AccountService accountService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.accountService = accountService;
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		return (StringUtils.hasText (bearerToken) && bearerToken.startsWith("Bearer "))?bearerToken.substring(7):null;

	};

	public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String tokenHeader = request.getHeader ("Authorization");
		String token = tokenHeader.replace("Bearer ", "");

		try{
			String user = jwtUtil.extractUsername (token);
			Accounts accounts = accountService.getAccountByUsername (user);
			return new UsernamePasswordAuthenticationToken (accounts, null);
		}catch (Exception e){
			return null;
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String jwtToken = getJwtFromRequest (request);

		if(jwtToken != null && jwtUtil.validateToken(jwtToken)) {
			UsernamePasswordAuthenticationToken authenticationToken = getAuthentication (request);
			SecurityContextHolder.getContext ().setAuthentication (authenticationToken);
		}
		filterChain.doFilter (request, response);
	}


}
