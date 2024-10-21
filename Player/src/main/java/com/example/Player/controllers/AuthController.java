package com.example.Player.controllers;

import com.example.Player.models.User;
import com.example.Player.services.UserService;
import com.example.Player.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        return userService.authenticate(username, password)
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername());
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Invalid username or password");
                    return ResponseEntity.status(401).body(errorResponse);
                });
    }

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");

        try {
            User newUser = userService.registerUser(username, email, password);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        if (isAvailable) {
            return ResponseEntity.ok("Username is available");
        } else {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Return a 401 Unauthorized if the Authorization header is missing or malformed
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Authorization header");
            return ResponseEntity.status(401).body(errorResponse);
        }

        // Extract token from "Bearer <token>" format
        String token = authHeader.substring(7);

        // Validate the token
        if (!jwtUtil.validateToken(token)) {
            // Return 401 if the token is invalid
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid token");
            return ResponseEntity.status(401).body(errorResponse);
        }

        // Extract username from the token
        String username = jwtUtil.getUsernameFromToken(token);
        return userService.findByUsername(username)
                .map(user -> {
                    // Returning the user directly
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "User not found");
                    return ResponseEntity.status(404).body((User) errorResponse);
                });
    }


}
