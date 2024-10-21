package com.example.Player.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToMany(mappedBy = "achievements")
    private List<FantasyTeam> fantasyTeams = new ArrayList<>();

    public Achievement() {
    }

    public Achievement(Long id, String name, String description, List<FantasyTeam> fantasyTeams) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fantasyTeams = fantasyTeams;
    }

    // Getters and Setters

    public List<FantasyTeam> getFantasyTeams() {
        return fantasyTeams;
    }

    public void setFantasyTeams(List<FantasyTeam> fantasyTeams) {
        this.fantasyTeams = fantasyTeams;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
