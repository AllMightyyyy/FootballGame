// src/main/java/com/example/Player/controllers/AuthController.java

package com.example.Player.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.Player.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Player.models.User;
import com.example.Player.services.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Ensure this matches your frontend
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // DTOs for requests and responses
    static class RegisterRequest {
        public String username;
        public String email;
        public String password;
    }

    static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.username, request.email, request.password);
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully.");
            response.put("token", token);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.authenticate(request.username, request.password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = jwtUtil.generateToken(user.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail()
            ));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Invalid username or password.")
            );
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        Map<String, Object> response = new HashMap<>();
        response.put("available", available);
        if (!available) {
            String suggestedUsername = userService.suggestUsername(username);
            response.put("suggestedUsername", suggestedUsername);
        }
        return ResponseEntity.ok(response);
    }
}
