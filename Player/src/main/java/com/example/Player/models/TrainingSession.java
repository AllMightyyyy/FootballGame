package com.example.Player.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fantasy_team_id", nullable = false)
    private FantasyTeam fantasyTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_player_id")
    private FantasyPlayer fantasyPlayer;

    @Enumerated(EnumType.STRING)
    private CoachType coachType;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TrainingSession() {
    }

    public TrainingSession(Long id, FantasyTeam fantasyTeam, FantasyPlayer fantasyPlayer, CoachType coachType, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.fantasyTeam = fantasyTeam;
        this.fantasyPlayer = fantasyPlayer;
        this.coachType = coachType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public FantasyPlayer getFantasyPlayer() {
        return fantasyPlayer;
    }

    public void setFantasyPlayer(FantasyPlayer fantasyPlayer) {
        this.fantasyPlayer = fantasyPlayer;
    }

    public CoachType getCoachType() {
        return coachType;
    }

    public void setCoachType(CoachType coachType) {
        this.coachType = coachType;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
