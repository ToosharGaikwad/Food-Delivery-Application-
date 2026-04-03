package com.FoodServe.Dilevery.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.Enum.Role;
import com.FoodServe.Dilevery.Userrepository.UserRepository;
import com.FoodServe.Dilevery.dto.LoginRequest;
import com.FoodServe.Dilevery.dto.RegisterRequest;
import com.FoodServe.Dilevery.entity.User;
import com.FoodServe.Dilevery.jwt.auth.JwtService;
import com.FoodServe.Dilevery.service.UserLoginSevice;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserLoginSevice userLoginService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(UserLoginSevice userLoginService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userLoginService = userLoginService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

   
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userLoginService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        // ✅ 1. Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // ✅ 2. Get role from DB
        Role role = user.getRole();

        // ✅ 3. Generate token (better: include role in token)
        String token = jwtService.generateToken(user.getEmail());

        // ✅ 4. Send response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", role.name()); // ✅ CORRECT

        return ResponseEntity.ok(response);
    }
}