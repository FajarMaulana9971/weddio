package com.weddio.weddio.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

	private static final String SECRET = "X+hdj8XlVPAfBnLLLtzAAgvElTFfeAQHo1tKv9yPEj7Oui+2MjHaYxhMmb3P6HrjkCUx1IsUa/5RhQpeS26gjg==";

	private final SecretKey secretKey = Keys.hmacShaKeyFor (SECRET. getBytes (StandardCharsets.UTF_8));

	public Claims extractAllClaims(String token){
		return Jwts.parserBuilder ().setSigningKey (secretKey).build().parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims (token);
		return claimsResolver.apply (claims);
	}

	public String extractUsername(String token) {
		return extractClaim (token, Claims::getSubject);
	}

	public Date extractExpiration(String token){
		return extractClaim (token, Claims::getExpiration);
	}

	public String extractIdToken(String token){
		Claims claims = extractAllClaims (token);
		return claims.get ("id", String.class);
	}

	public String createToken(Map<String, Object> claims, String subject){
		return Jwts.builder ()
				.setClaims (claims)
				.setSubject (subject)
				.setIssuedAt (new Date (System.currentTimeMillis ()))
				.setExpiration (new Date (System.currentTimeMillis () + 1800000))
				.signWith (secretKey, SignatureAlgorithm.HS256).compact ();
	}

	public String createToken(String oldToken, String subject){
		Claims claims= Jwts.parserBuilder ().setSigningKey (secretKey).build().parseClaimsJws(oldToken).getBody();

		return Jwts.builder ()
				.setClaims (claims)
				.setSubject (subject)
				.setIssuedAt (new Date (System.currentTimeMillis ()))
				.setExpiration (new Date (System.currentTimeMillis () + 1800000))
				.signWith (secretKey, SignatureAlgorithm.HS256).compact ();
	}

	public String createRefreshToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

	public Boolean validateToken(String token) {
		try {
			String cleanToken = token.replace("Bearer ", "");
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(cleanToken);
			return true;
		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	public Boolean isTokenExpired(String token) {
		return extractExpirationDate(token).before(new Date());
	}

	private Date extractExpirationDate(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		return claims.getExpiration();
	}
}
