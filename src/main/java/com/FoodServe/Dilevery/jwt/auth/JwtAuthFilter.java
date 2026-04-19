package com.FoodServe.Dilevery.jwt.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.FoodServe.Dilevery.service.BlacklistService;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  
	private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final BlacklistService blacklistService;

    public JwtAuthFilter(JwtService jwtService,
    		BlacklistService blacklistService,
                         UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.blacklistService = blacklistService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2️⃣ ✅ KEY FIX: If no token, skip JWT processing entirely
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	 System.out.println("header problem"+authHeader);
            filterChain.doFilter(request, response); // just move on
            
            return;
           
        }

        // 3️⃣ Extract the token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // 4️⃣ Guard against empty token string after stripping prefix
        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            System.out.println("token is blank");
            return;
        }
        
        // 5️⃣ Extract username from token
        String username = jwtService.extractUsername(token);

        // 6️⃣ Only authenticate if not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (blacklistService.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
         // ✅ CLEANER — use the authorities already on UserDetails (from User.getAuthorities())
            if (jwtService.isTokenValid(token, userDetails)) {
                // ✅ Extract role from JWT token claims (not from database)
                Claims claims = jwtService.extractAllClaims(token);
                String role = (String) claims.get("role");
                
                // ✅ Create authorities from JWT role
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    System.out.println("✅ Role from JWT: " + role);
                }
                
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities // ✅ Use JWT authorities
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
    
}