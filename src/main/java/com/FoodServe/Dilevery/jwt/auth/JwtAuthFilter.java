package com.FoodServe.Dilevery.jwt.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    public JwtAuthFilter(
            JwtService jwtService,
            BlacklistService blacklistService,
            UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ Skip JWT validation for receipt endpoint
        if (request.getRequestURI().contains("/receipt")) {

            System.out.println("Skipping JWT for receipt endpoint");

            filterChain.doFilter(request, response);

            return;
        }

        // ✅ Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // ✅ If no token, continue request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("No Bearer token found");

            filterChain.doFilter(request, response);

            return;
        }

        // ✅ Extract token
        String token = authHeader.substring(7);

        // ✅ Blank token check
        if (token.isBlank()) {

            System.out.println("Token is blank");

            filterChain.doFilter(request, response);

            return;
        }

        String username = null;

        // ✅ Handle expired/invalid JWT safely
        try {

            username = jwtService.extractUsername(token);

        } catch (Exception e) {

            System.out.println("JWT Error: " + e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Token Expired or Invalid");

            return;
        }

        // ✅ Authenticate user
        if (username != null
                && SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // ✅ Blacklist check
            if (blacklistService.isBlacklisted(token)) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                response.getWriter().write("Token Blacklisted");

                return;
            }

            // ✅ Validate token
            if (jwtService.isTokenValid(token, userDetails)) {

                Claims claims =
                        jwtService.extractAllClaims(token);

                String role =
                        (String) claims.get("role");

                List<SimpleGrantedAuthority> authorities =
                        new ArrayList<>();

                if (role != null) {

                    authorities.add(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + role
                            )
                    );

                    System.out.println("✅ Role from JWT: " + role);
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}