package com.weddio.weddio.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
		httpSecurity.csrf (csrf -> csrf.disable ())
				.authorizeHttpRequests (auth -> auth
						.requestMatchers ("/auth/**").permitAll ()
						.requestMatchers ("/public/**").permitAll ()
						.anyRequest ().authenticated ()
				)
				.sessionManagement (session -> session
						.sessionCreationPolicy (SessionCreationPolicy.STATELESS)
				);
		httpSecurity.addFilterBefore (new );

		return httpSecurity.build ();
	}
}
