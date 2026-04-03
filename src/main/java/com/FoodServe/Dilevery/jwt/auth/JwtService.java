package com.FoodServe.Dilevery.jwt.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.FoodServe.Dilevery.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static String secret = "mySuperSecretKeyForJwtAuthentication12345";

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

   

    // ✅ GENERATE TOKEN
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }



	public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}



	private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
}



	private Date extractExpiration(String token) {
	    return Jwts.parser()
	            .setSigningKey(secret)
	            .parseClaimsJws(token)
	            .getBody()
	            .getExpiration();
	}



	
    
}