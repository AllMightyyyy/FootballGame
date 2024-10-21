package com.example.Player.models;

import jakarta.persistence.*;

@Entity
public class FantasyMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private FantasyTeam team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private FantasyTeam team2;

    private int team1Score;
    private int team2Score;

    private String status;  // e.g., "completed", "scheduled", etc.

    // Getters and setters
    public void setTeam1(FantasyTeam team1) {
        this.team1 = team1;
    }

    public void setTeam2(FantasyTeam team2) {
        this.team2 = team2;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTeam1Score(int score) {
        this.team1Score = score;
    }

    public void setTeam2Score(int score) {
        this.team2Score = score;
    }

    // Additional methods as needed
}

