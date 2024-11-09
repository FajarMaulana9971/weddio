package com.weddio.weddio.configs;

import com.weddio.weddio.services.interfaces.AccountService;
import com.weddio.weddio.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

	private final AccountService accountService;
	private final JwtUtil jwtUtil;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager ();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
		return httpSecurity
				.csrf (AbstractHttpConfigurer::disable)
				.authorizeHttpRequests (authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers ("/auth/**").permitAll ()
						.requestMatchers ("/public/**").permitAll ()
						.anyRequest ().permitAll ()
				)
				.addFilter (new AuthenticationFilter (authenticationManager (httpSecurity.getSharedObject (AuthenticationConfiguration.class)), jwtUtil, accountService))
				.sessionManagement (sessionManagement -> sessionManagement.sessionCreationPolicy (SessionCreationPolicy.STATELESS))
				.build ();
	}
}
