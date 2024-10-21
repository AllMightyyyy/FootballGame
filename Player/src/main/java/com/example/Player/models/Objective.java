package com.example.Player.models;

import jakarta.persistence.*;

@Entity
@Table(name = "objectives")
public class Objective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_team_id", nullable = false)
    private FantasyTeam fantasyTeam;

    private boolean achieved;

    public Objective() {
    }

    public Objective(Long id, String description, FantasyTeam fantasyTeam, boolean achieved) {
        this.id = id;
        this.description = description;
        this.fantasyTeam = fantasyTeam;
        this.achieved = achieved;
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

    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }
}
