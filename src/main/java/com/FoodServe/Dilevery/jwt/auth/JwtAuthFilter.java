package com.FoodServe.Dilevery.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.FoodServe.Dilevery.Security.CustomUserDetailsService;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                               HttpServletResponse response,
	                               FilterChain filterChain)
	        throws ServletException, IOException {

	    final String authHeader = request.getHeader("Authorization");

	    String jwt = null;
	    String username = null;

	    // 1. Extract token
	    if (authHeader != null && authHeader.startsWith("Bearer")) {
	        jwt = authHeader.substring(7);
	        username = jwtService.extractUsername(jwt);
	    }

	    // 2. Authenticate user
	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	        if (jwtService.isTokenValid(jwt, userDetails)) {

	            UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(
	                            userDetails,
	                            null,
	                            userDetails.getAuthorities()
	                    );

	            authToken.setDetails(
	                    new WebAuthenticationDetailsSource().buildDetails(request)
	            );

	            // 🔥 MOST IMPORTANT LINE
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	        }
	    }

	    filterChain.doFilter(request, response);
	}
}
