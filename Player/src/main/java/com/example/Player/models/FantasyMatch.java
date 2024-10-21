// FantasyMatch.java
package com.example.Player.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fantasy_matches", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"league_id", "team1_id", "team2_id", "scheduled_at"})
})
public class FantasyMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int team1Score;
    private int team2Score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id")
    private FantasyTeam team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id")
    private FantasyTeam team2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id")
    private FantasyLeague league;

    private String status; // e.g., "scheduled", "completed"

    @Column(name = "events", columnDefinition = "TEXT")
    private String events; // Serialized events

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    // Constructors, Getters, and Setters

    public FantasyMatch() {
    }

    // Getters and Setters...

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public FantasyTeam getTeam1() {
        return team1;
    }

    public void setTeam1(FantasyTeam team1) {
        this.team1 = team1;
    }

    public FantasyTeam getTeam2() {
        return team2;
    }

    public void setTeam2(FantasyTeam team2) {
        this.team2 = team2;
    }

    public FantasyLeague getLeague() {
        return league;
    }

    public void setLeague(FantasyLeague league) {
        this.league = league;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
