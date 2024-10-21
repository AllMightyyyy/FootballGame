package com.example.Player.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stadiums")
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stadiumName;
    private int capacity;
    private double pitchQuality;
    private double trainingFacilities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_team_id", nullable = false)
    private FantasyTeam fantasyTeam;

    private int capacityLevel;
    private int pitchLevel;
    private int trainingLevel;

    public Stadium() {
    }

    public Stadium(Long id, String stadiumName, int capacity, double pitchQuality, double trainingFacilities, FantasyTeam fantasyTeam) {
        this.id = id;
        this.stadiumName = stadiumName;
        this.capacity = capacity;
        this.pitchQuality = pitchQuality;
        this.trainingFacilities = trainingFacilities;
        this.fantasyTeam = fantasyTeam;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPitchQuality() {
        return pitchQuality;
    }

    public void setPitchQuality(double pitchQuality) {
        this.pitchQuality = pitchQuality;
    }

    public double getTrainingFacilities() {
        return trainingFacilities;
    }

    public void setTrainingFacilities(double trainingFacilities) {
        this.trainingFacilities = trainingFacilities;
    }

    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public int getTrainingLevel() {
        return trainingLevel;
    }

    public void setTrainingLevel(int trainingLevel) {
        this.trainingLevel = trainingLevel;
    }

    public int getPitchLevel() {
        return pitchLevel;
    }

    public void setPitchLevel(int pitchLevel) {
        this.pitchLevel = pitchLevel;
    }

    public int getCapacityLevel() {
        return capacityLevel;
    }

    public void setCapacityLevel(int capacityLevel) {
        this.capacityLevel = capacityLevel;
    }
}
