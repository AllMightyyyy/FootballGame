// Lineup.java
package com.example.Player.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.Optional;

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

    // Constructors
    public Lineup() {
    }

    public Lineup(String formation, List<String> specialists, List<FantasyPlayer> starting11, List<FantasyPlayer> substitutes) {
        this.formation = formation;
        this.specialists = specialists;
        this.starting11 = starting11;
        this.substitutes = substitutes;
    }

    // Getters and Setters

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public List<String> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(List<String> specialists) {
        this.specialists = specialists;
    }

    public List<FantasyPlayer> getStarting11() {
        return starting11;
    }

    public void setStarting11(List<FantasyPlayer> starting11) {
        this.starting11 = starting11;
    }

    public List<FantasyPlayer> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<FantasyPlayer> substitutes) {
        this.substitutes = substitutes;
    }

    /**
     * Assigns a player to a specific position in the starting 11.
     * Here, we simplify by not tying positions to specific player slots.
     * Enhance this method to map specific positions (e.g., "GK", "LB") to players based on the formation.
     */
    public boolean assignPlayerToPosition(FantasyPlayer player, String position) {
        if (starting11.size() >= 11) {
            return false; // Starting 11 is full
        }
        if (starting11.contains(player)) {
            return false; // Player already in starting 11
        }
        // Additional logic can be implemented to check if player can play the position
        starting11.add(player);
        return true;
    }

    /**
     * Removes a player from the starting 11.
     */
    public boolean removePlayerFromStarting11(FantasyPlayer player) {
        return starting11.remove(player);
    }

    /**
     * Adds a player to substitutes.
     */
    public boolean addSubstitute(FantasyPlayer player) {
        if (substitutes.size() >= 7) { // Typically, 7 substitutes
            return false;
        }
        if (substitutes.contains(player)) {
            return false;
        }
        substitutes.add(player);
        return true;
    }

    /**
     * Removes a player from substitutes.
     */
    public boolean removeSubstitute(FantasyPlayer player) {
        return substitutes.remove(player);
    }

    /**
     * Substitutes a player in the starting 11 with a player from substitutes.
     */
    public boolean substitutePlayer(FantasyPlayer outPlayer, FantasyPlayer inPlayer) {
        if (starting11.contains(outPlayer) && substitutes.contains(inPlayer)) {
            starting11.remove(outPlayer);
            substitutes.remove(inPlayer);
            starting11.add(inPlayer);
            substitutes.add(outPlayer);
            return true;
        }
        return false;
    }

    /**
     * Retrieves players by their specialist roles.
     */
    public List<FantasyPlayer> getPlayersBySpecialistRole(String role) {
        List<FantasyPlayer> specialists = new ArrayList<>();
        for (FantasyPlayer player : starting11) {
            switch (role.toUpperCase()) {
                case "PENALTY_TAKER":
                    if (player.isPenaltyTaker()) {
                        specialists.add(player);
                    }
                    break;
                case "CORNER_TAKER":
                    if (player.isCornerTaker()) {
                        specialists.add(player);
                    }
                    break;
                case "FREEKICK_TAKER":
                    if (player.isFreeKickTaker()) {
                        specialists.add(player);
                    }
                    break;
                default:
                    break;
            }
        }
        return specialists;
    }

    // Additional methods as needed
}
