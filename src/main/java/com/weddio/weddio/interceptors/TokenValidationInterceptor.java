package com.weddio.weddio.interceptors;

import com.weddio.weddio.services.AuthUserDetailService;
import com.weddio.weddio.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenValidationInterceptor extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final AuthUserDetailService authUserDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
//		masuk sini
		String jwt = parseJwt(request);
		if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
//		jalan if
			String username = jwtUtil.getUserNameFromJwtToken(jwt);

			UserDetails userDetails = authUserDetailService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
			);

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		String token = null;

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			token = headerAuth.substring(7);
		}
		else {
			if(request.getCookies () != null){
				for (Cookie cookie : request.getCookies()) {
					if (cookie.getName().equals("accessToken")) {
						token = cookie.getValue();
						break;
					}
				}
			}
		}
		return token;
	}
}
