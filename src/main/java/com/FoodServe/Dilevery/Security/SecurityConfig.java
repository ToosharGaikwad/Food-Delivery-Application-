package com.FoodServe.Dilevery.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.*;

import java.util.List;

import com.FoodServe.Dilevery.jwt.auth.JwtAuthFilter;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth	
            	    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            	    .requestMatchers("/api/users/login", "/logout", "/api/users/register").permitAll()
            	    // ✅ DELETE requires ADMIN
            	    .requestMatchers(HttpMethod.DELETE, "/res/id/**").hasRole("ADMIN")
            	    // ✅ Public GET endpoints
            	    .requestMatchers("/res/allRestaurant").permitAll()
            	    .requestMatchers("/res/{id}").permitAll()
            	    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
            	    .requestMatchers("/orders").permitAll()
            	    // ✅ ADMIN-only POST/PUT operations
            	    .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
            	    .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
            	    .requestMatchers(HttpMethod.PATCH,"/orders").hasRole("ADMIN")
            		.requestMatchers(HttpMethod.GET,"/orders").hasAnyRole("USER","ADMIN")
            	    
            	    
            	    .anyRequest().authenticated()
            	)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}