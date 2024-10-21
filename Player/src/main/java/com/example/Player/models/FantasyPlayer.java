package com.example.Player.models;

import jakarta.persistence.*;

@Entity
@Table(name = "fantasy_players")
public class FantasyPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "real_player_id", nullable = false)
    private Player realPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_league_id", nullable = false)
    private FantasyLeague fantasyLeague;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_team_id")
    private FantasyTeam fantasyTeam; // Null if free agent

    private double price; // Market price for transfers

    private boolean isInjured = false;

    private double stamina; // Value between 0 and 100

    // Additional fields like position-specific stats


    public FantasyPlayer() {
    }

    public FantasyPlayer(double stamina, boolean isInjured, double price, FantasyTeam fantasyTeam, FantasyLeague fantasyLeague, Player realPlayer, Long id) {
        this.stamina = stamina;
        this.isInjured = isInjured;
        this.price = price;
        this.fantasyTeam = fantasyTeam;
        this.fantasyLeague = fantasyLeague;
        this.realPlayer = realPlayer;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
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
}
