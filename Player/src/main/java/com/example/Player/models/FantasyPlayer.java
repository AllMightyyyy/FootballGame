// FantasyPlayer.java
package com.example.Player.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fantasy_players")
public class FantasyPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "real_player_id", nullable = false)
    private Player realPlayer;

    @ManyToOne
    @JoinColumn(name = "fantasy_league_id")
    private FantasyLeague fantasyLeague;

    @ManyToOne
    @JoinColumn(name = "fantasy_team_id")
    private FantasyTeam fantasyTeam;

    private double price;

    private double stamina;

    private boolean injured;

    private boolean assigned;

    // Specialist Fields
    private boolean penaltyTaker;
    private boolean cornerTaker;
    private boolean freeKickTaker;

    // Constructors
    public FantasyPlayer() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Player getRealPlayer() {
        return realPlayer;
    }

    public void setRealPlayer(Player realPlayer) {
        this.realPlayer = realPlayer;
    }

    public FantasyLeague getFantasyLeague() {
        return fantasyLeague;
    }

    public void setFantasyLeague(FantasyLeague fantasyLeague) {
        this.fantasyLeague = fantasyLeague;
    }

    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public boolean isInjured() {
        return injured;
    }

    public void setInjured(boolean injured) {
        this.injured = injured;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public boolean isPenaltyTaker() {
        return penaltyTaker;
    }

    public void setPenaltyTaker(boolean penaltyTaker) {
        this.penaltyTaker = penaltyTaker;
    }

    public boolean isCornerTaker() {
        return cornerTaker;
    }

    public void setCornerTaker(boolean cornerTaker) {
        this.cornerTaker = cornerTaker;
    }

    public boolean isFreeKickTaker() {
        return freeKickTaker;
    }

    public void setFreeKickTaker(boolean freeKickTaker) {
        this.freeKickTaker = freeKickTaker;
    }

    // Additional methods as needed
}
