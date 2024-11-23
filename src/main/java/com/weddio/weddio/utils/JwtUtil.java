package com.weddio.weddio.utils;

import com.weddio.weddio.models.Accounts;
import com.weddio.weddio.services.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@AllArgsConstructor
@Slf4j
public class JwtUtil {

	private final JwtProperties jwtProperties;

	private Key accessTokenKey() {
		String secret = jwtProperties.getAccessToken().getSecret();

		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(accessTokenKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(accessTokenKey()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public Date getExpirationDate(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(accessTokenKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getExpiration();
	}

	public String generateAccessToken(Map<String, Object> claims, Accounts accounts) {
		int expirationInMinutes = jwtProperties.getAccessToken().getExpirationInMinutes();
		LocalDateTime issuedAt = LocalDateTime.now();
		LocalDateTime expirationAt = issuedAt.plusMinutes(expirationInMinutes);

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(accounts.getUsername())
				.setIssuedAt(Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(expirationAt.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(accessTokenKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public Integer getUserIdFromJwtToken(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(accessTokenKey())
				.build()
				.parseClaimsJws(token)
				.getBody();

		return claims.get("id", Integer.class);
	}
}
