package com.example.Player.controllers;

import com.example.Player.DTO.RegisterResponseDTO;
import com.example.Player.DTO.TeamDTO;
import com.example.Player.DTO.UserDTO;
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
                    // Convert User to UserDTO
                    UserDTO userDTO = convertToUserDTO(user);
                    return ResponseEntity.ok(userDTO);
                })
                .orElseGet(() -> {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "User not found");
                    return ResponseEntity.status(404).body((UserDTO) errorResponse);
                });
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");

        try {
            User newUser = userService.registerUser(username, email, password);

            // Generate JWT token for the new user
            String token = jwtUtil.generateToken(newUser.getUsername());

            // Convert User to UserDTO
            UserDTO userDTO = convertToUserDTO(newUser);

            // Create RegisterResponseDTO
            RegisterResponseDTO responseDTO = new RegisterResponseDTO(token, userDTO);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        Map<String, Object> response = new HashMap<>();
        response.put("available", isAvailable);
        if (!isAvailable) {
            String suggestedUsername = userService.suggestUsername(username);
            response.put("suggestedUsername", suggestedUsername);
        }
        return ResponseEntity.ok(response);
    }

    // Utility method to convert User to UserDTO
    private UserDTO convertToUserDTO(User user) {
        if (user.getTeam() != null) {
            // Retrieve league code via League relationship
            String leagueCode = user.getTeam().getLeague().getCode();
            TeamDTO teamDTO = new TeamDTO(user.getTeam().getId(), user.getTeam().getName(), leagueCode);
            return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), teamDTO);
        }
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), null);
    }
}
