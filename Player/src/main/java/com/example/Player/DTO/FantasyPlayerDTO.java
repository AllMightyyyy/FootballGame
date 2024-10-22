package com.example.Player.DTO;

public class FantasyPlayerDTO {
    private Long id;
    private String name;
    private String position;
    // Add other relevant fields

    // Constructors
    public FantasyPlayerDTO() {}

    public FantasyPlayerDTO(Long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    // Getters and Setters

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getPosition(){
        return position;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPosition(String position){
        this.position = position;
    }
}
