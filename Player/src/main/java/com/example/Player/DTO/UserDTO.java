// src/main/java/com/example/Player/dto/UserDTO.java

package com.example.Player.DTO;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private TeamDTO team; // Assuming you have a TeamDTO

    // Constructors, Getters, and Setters

    public UserDTO() {}

    public UserDTO(Long id, String username, String email, TeamDTO team) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }
}
