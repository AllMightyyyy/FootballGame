// FantasyMatch.java
package com.example.Player.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "fantasy_matches")
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

    private LocalDate scheduledDate;

    private String status; // e.g., "scheduled", "completed"

    private int team1Score;
    private int team2Score;

    @Lob
    private String events; // Serialized events

    // Constructors
    public FantasyMatch() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public FantasyTeam getTeam1() {
        return team1;
    }

    public FantasyTeam getTeam2() {
        return team2;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public void setTeam1(FantasyTeam team1) {
        this.team1 = team1;
    }

    public void setTeam2(FantasyTeam team2) {
        this.team2 = team2;
    }

    // Additional methods as needed
}
