package com.example.Player.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Lineup {
    private String formation; // e.g., "4-3-3", "3-5-2"

    @ElementCollection
    @CollectionTable(name = "lineup_specialists", joinColumns = @JoinColumn(name = "fantasy_team_id"))
    @Column(name = "specialist")
    private List<String> specialists = new ArrayList<>(); // e.g., "Penalty Taker", "Corner Taker"

    @OneToMany
    @JoinTable(
            name = "starting_11",
            joinColumns = @JoinColumn(name = "fantasy_team_id"),
            inverseJoinColumns = @JoinColumn(name = "fantasy_player_id")
    )
    private List<FantasyPlayer> starting11 = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "substitutes",
            joinColumns = @JoinColumn(name = "fantasy_team_id"),
            inverseJoinColumns = @JoinColumn(name = "fantasy_player_id")
    )
    private List<FantasyPlayer> substitutes = new ArrayList<>();

    public Lineup() {
    }

    public Lineup(String formation, List<String> specialists, List<FantasyPlayer> starting11, List<FantasyPlayer> substitutes) {
        this.formation = formation;
        this.specialists = specialists;
        this.starting11 = starting11;
        this.substitutes = substitutes;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public List<FantasyPlayer> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<FantasyPlayer> substitutes) {
        this.substitutes = substitutes;
    }

    public List<FantasyPlayer> getStarting11() {
        return starting11;
    }

    public void setStarting11(List<FantasyPlayer> starting11) {
        this.starting11 = starting11;
    }

    public List<String> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(List<String> specialists) {
        this.specialists = specialists;
    }
}
