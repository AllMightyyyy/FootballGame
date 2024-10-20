// src/main/java/com/example/Player/utils/TeamDTO.java

package com.example.Player.DTO;

public class TeamDTO {
    private Long id;
    private String name;

    // Constructors
    public TeamDTO() {}

    public TeamDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
