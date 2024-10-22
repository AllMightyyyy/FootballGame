// Achievement.java
package com.example.Player.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToMany(mappedBy = "achievements")
    private Set<FantasyTeam> fantasyTeams = new HashSet<>();

    // Constructors
    public Achievement() {
    }

    public Achievement(String description) {
        this.description = description;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<FantasyTeam> getFantasyTeams() {
        return fantasyTeams;
    }

    public void setFantasyTeams(Set<FantasyTeam> fantasyTeams) {
        this.fantasyTeams = fantasyTeams;
    }
}
