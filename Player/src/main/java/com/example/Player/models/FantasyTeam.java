// src/main/java/com/example/Player/models/FantasyTeam.java

package com.example.Player.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "fantasy_teams")
public class FantasyTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    private double balance;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "fantasy_league_id")
    private FantasyLeague fantasyLeague;

    @ManyToMany
    @JoinTable(
            name = "team_achievements",
            joinColumns = @JoinColumn(name = "fantasy_team_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    private Set<Achievement> achievements = new HashSet<>();

    @Embedded
    private Lineup lineup;

    @OneToMany(mappedBy = "fantasyTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sponsor> sponsors = new ArrayList<>();

    @OneToMany(mappedBy = "fantasyTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Objective> objectives = new ArrayList<>();

    @OneToOne(mappedBy = "fantasyTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stadium stadium;

    @OneToMany(mappedBy = "fantasyTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FantasyPlayer> players = new ArrayList<>();

    // Constructors
    public FantasyTeam() {
    }

    public FantasyTeam(String teamName, Team team, FantasyLeague fantasyLeague, User owner) {
        this.teamName = teamName;
        this.team = team;
        this.fantasyLeague = fantasyLeague;
        this.owner = owner;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    // Added setter for id
    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public double getBalance() {
        return balance;
    }

    public User getOwner() {
        return owner;
    }

    public FantasyLeague getFantasyLeague() {
        return fantasyLeague;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Lineup getLineup() {
        return lineup;
    }

    public void setLineup(Lineup lineup) {
        this.lineup = lineup;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public List<FantasyPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<FantasyPlayer> players) {
        this.players = players;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setFantasyLeague(FantasyLeague fantasyLeague) {
        this.fantasyLeague = fantasyLeague;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
