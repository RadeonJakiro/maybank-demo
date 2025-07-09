package com.maybank.liangquan.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtTokenProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${security.jwt.token.secret.key}")
	private String secretKey;

	public String createToken(String userId, String email, String mobileNo, String fullName, List<String> roles,
			Date now, Date expiredOn) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userId);
		claims.put("email", email);
		claims.put("mobileNo", mobileNo);
		claims.put("fullName", fullName);
		claims.put("roles", roles);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiredOn)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public String getUsername(String token) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);

		// Find username
		LOGGER.debug("Show Username : {}, FullBody : {}", claims.getBody().get("username", String.class));
		return claims.getBody().get("username", String.class);
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);

			return !claims.getBody().getExpiration().before(new Date());
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty");
		}
		return false;
	}

}
