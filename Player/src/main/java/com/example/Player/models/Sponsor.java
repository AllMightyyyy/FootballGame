package com.example.Player.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sponsors")
public class Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sponsorName;
    private double contribution; // e.g., 104000 per matchday
    private int contractDuration; // in matchdays

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_team_id", nullable = false)
    private FantasyTeam fantasyTeam;

    private String direction; // e.g., "North", "South", etc.

    public Sponsor() {
    }

    public Sponsor(Long id, String sponsorName, double contribution, int contractDuration, FantasyTeam fantasyTeam, String direction) {
        this.id = id;
        this.sponsorName = sponsorName;
        this.contribution = contribution;
        this.contractDuration = contractDuration;
        this.fantasyTeam = fantasyTeam;
        this.direction = direction;
    }

    // Getters and Setters

    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }

    public int getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(int contractDuration) {
        this.contractDuration = contractDuration;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

