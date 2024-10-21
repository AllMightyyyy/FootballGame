// src/main/java/com/example/Player/dto/RegisterResponseDTO.java

package com.example.Player.DTO;

import com.example.Player.models.User;

public class RegisterResponseDTO {
    private String token;
    private UserDTO user;

    // Constructors
    public RegisterResponseDTO() {}

    public RegisterResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    // Getters and Setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
