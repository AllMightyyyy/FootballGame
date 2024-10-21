package com.example.Player.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendly_matches")
public class FriendlyMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team1_id", nullable = false)
    private FantasyTeam team1;

    @ManyToOne
    @JoinColumn(name = "team2_id", nullable = false)
    private FantasyTeam team2;

    private String status; // REQUESTED, ACCEPTED, COMPLETED, DECLINED

    private LocalDateTime requestedAt;

    private LocalDateTime scheduledAt;

    public FriendlyMatch() {
    }

    public FriendlyMatch(Long id, FantasyTeam team1, FantasyTeam team2, String status, LocalDateTime requestedAt, LocalDateTime scheduledAt) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.status = status;
        this.requestedAt = requestedAt;
        this.scheduledAt = scheduledAt;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
