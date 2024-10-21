package com.example.Player.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fantasy_leagues", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"real_league_id"})
})
public class FantasyLeague {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "English Premier League Fantasy"

    @OneToOne
    @JoinColumn(name = "real_league_id", referencedColumnName = "id", nullable = false)
    private League realLeague;

    @OneToMany(mappedBy = "fantasyLeague", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FantasyTeam> fantasyTeams = new ArrayList<>();

    @OneToMany(mappedBy = "fantasyLeague", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FantasyPlayer> availablePlayers = new ArrayList<>();

    // Additional fields like season, rules, budget caps can be added here


    public FantasyLeague() {
    }

    public FantasyLeague(List<FantasyPlayer> availablePlayers, List<FantasyTeam> fantasyTeams, League realLeague, String name, Long id) {
        this.availablePlayers = availablePlayers;
        this.fantasyTeams = fantasyTeams;
        this.realLeague = realLeague;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public League getRealLeague() {
        return realLeague;
    }

    public void setRealLeague(League realLeague) {
        this.realLeague = realLeague;
    }

    public List<FantasyTeam> getFantasyTeams() {
        return fantasyTeams;
    }

    public void setFantasyTeams(List<FantasyTeam> fantasyTeams) {
        this.fantasyTeams = fantasyTeams;
    }

    public List<FantasyPlayer> getAvailablePlayers() {
        return availablePlayers;
    }

    public void setAvailablePlayers(List<FantasyPlayer> availablePlayers) {
        this.availablePlayers = availablePlayers;
    }
}
