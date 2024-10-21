package com.example.Player.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fantasy_teams")
public class FantasyTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;

    private double balance; // Controlled by the team's stature, sponsors, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_league_id", nullable = false)
    private FantasyLeague fantasyLeague;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User owner; // One-to-one relationship with User

    @OneToMany(mappedBy = "fantasyTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FantasyPlayer> players = new ArrayList<>();

    @Embedded
    private Lineup lineup; // Composition of formation, specialists, starting 11, substitutes

    @ManyToOne
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @OneToMany(mappedBy = "fantasyTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sponsor> sponsors = new ArrayList<>();

    private String objective; // e.g., "Win the League", "Qualify for Champions League"

    // Additional fields for objectives, income sources, etc.


    public FantasyTeam() {
    }

    public FantasyTeam(String teamName, Long id, double balance, FantasyLeague fantasyLeague, User owner, List<FantasyPlayer> players, Lineup lineup, Stadium stadium, List<Sponsor> sponsors, String objective) {
        this.teamName = teamName;
        this.id = id;
        this.balance = balance;
        this.fantasyLeague = fantasyLeague;
        this.owner = owner;
        this.players = players;
        this.lineup = lineup;
        this.stadium = stadium;
        this.sponsors = sponsors;
        this.objective = objective;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public FantasyLeague getFantasyLeague() {
        return fantasyLeague;
    }

    public void setFantasyLeague(FantasyLeague fantasyLeague) {
        this.fantasyLeague = fantasyLeague;
    }

    public List<FantasyPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<FantasyPlayer> players) {
        this.players = players;
    }

    public Lineup getLineup() {
        return lineup;
    }

    public void setLineup(Lineup lineup) {
        this.lineup = lineup;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }
}
