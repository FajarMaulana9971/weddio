package com.weddio.weddio.configs;

import com.weddio.weddio.interceptors.TokenValidationInterceptor;
import com.weddio.weddio.services.interfaces.AccountService;
import com.weddio.weddio.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig{

	private final TokenValidationInterceptor tokenValidationInterceptor;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.cors (AbstractHttpConfigurer::disable)
				.exceptionHandling(configurer ->
						configurer.authenticationEntryPoint((request, response, exception) ->
								handlerExceptionResolver.resolveException(request, response, null, exception)
						)
				)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers (HttpMethod.POST, "/login").permitAll ()
						.requestMatchers (HttpMethod.POST, "/register").permitAll ()
						.requestMatchers (HttpMethod.POST, "/public/**").permitAll ()
						.requestMatchers (HttpMethod.GET, "/public/**").permitAll ()
						.requestMatchers (HttpMethod.PUT, "/public/**").permitAll ()
						.anyRequest().authenticated ()
				)
				.sessionManagement((configurer) -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(tokenValidationInterceptor, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
