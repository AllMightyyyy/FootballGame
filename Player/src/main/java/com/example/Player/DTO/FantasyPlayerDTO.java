package com.example.Player.DTO;

public class FantasyPlayerDTO {
    private Long id;
    private String name;
    private String position; // This will be a comma-separated list of positions

    // Constructors
    public FantasyPlayerDTO() {}

    public FantasyPlayerDTO(Long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
