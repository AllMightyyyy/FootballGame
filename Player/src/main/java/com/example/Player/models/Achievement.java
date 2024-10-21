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

    private boolean achieved;

    // Constructors
    public Achievement() {
    }

    public Achievement(String description) {
        this.description = description;
        this.achieved = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Set<FantasyTeam> getFantasyTeams() {
        return fantasyTeams;
    }

    public void setFantasyTeams(Set<FantasyTeam> fantasyTeams) {
        this.fantasyTeams = fantasyTeams;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
