package com.profiteer.core.api.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

	public String extractUsername(String token) {
		try {
			token = token.replaceAll("Bearer ", "");
			return extractClaim(token.trim(), Claims::getSubject);
		} catch (Exception e) {
			log.info("extractUsername :: {}", e.getMessage());
			return null;
		}
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(FamousConstant.SECRET).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(String username, long expireTime) {
		log.info("User Name :: {}", username);
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username, expireTime);
	}

	public String createToken(Map<String, Object> claims, String subject, long expireTime) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(expireTime)).signWith(SignatureAlgorithm.HS256, FamousConstant.SECRET)
				.compact();
	}

	public Boolean validateToken(String token, String userName) {

		final String username = extractUsername(token);
		log.info("Validate User Name :: {}", userName);

		if (StringUtils.hasLength(username))
			return (username.equals(userName) && !isTokenExpired(token));

		return false;
	}
}