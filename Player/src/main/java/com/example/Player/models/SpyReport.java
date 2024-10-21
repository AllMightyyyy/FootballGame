package com.example.Player.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "spy_reports")
public class SpyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporting_team_id", nullable = false)
    private FantasyTeam reportingTeam;

    @ManyToOne
    @JoinColumn(name = "target_team_id", nullable = false)
    private FantasyTeam targetTeam;

    private String tactics; // Serialized or detailed tactics info
    private String playerStatus; // Serialized player stats

    private LocalDateTime reportedAt;

    public SpyReport() {
    }

    public SpyReport(Long id, String playerStatus, FantasyTeam reportingTeam, FantasyTeam targetTeam, String tactics, LocalDateTime reportedAt) {
        this.id = id;
        this.playerStatus = playerStatus;
        this.reportingTeam = reportingTeam;
        this.targetTeam = targetTeam;
        this.tactics = tactics;
        this.reportedAt = reportedAt;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FantasyTeam getReportingTeam() {
        return reportingTeam;
    }

    public void setReportingTeam(FantasyTeam reportingTeam) {
        this.reportingTeam = reportingTeam;
    }

    public FantasyTeam getTargetTeam() {
        return targetTeam;
    }

    public void setTargetTeam(FantasyTeam targetTeam) {
        this.targetTeam = targetTeam;
    }

    public String getTactics() {
        return tactics;
    }

    public void setTactics(String tactics) {
        this.tactics = tactics;
    }

    public String getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(String playerStatus) {
        this.playerStatus = playerStatus;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }
}
