package com.example.Player.DTO;

public class FantasyTeamDTO {
    private Long id;
    private String teamName;
    private double balance;
    private TeamDTO team;
    private UserDTO owner;
    private FantasyLeagueDTO fantasyLeague;

    // Constructors, Getters, and Setters

    public FantasyTeamDTO() {}

    public FantasyTeamDTO(Long id, String teamName, double balance, TeamDTO team, UserDTO owner, FantasyLeagueDTO fantasyLeague) {
        this.id = id;
        this.teamName = teamName;
        this.balance = balance;
        this.team = team;
        this.owner = owner;
        this.fantasyLeague = fantasyLeague;
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

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public FantasyLeagueDTO getFantasyLeague() {
        return fantasyLeague;
    }

    public void setFantasyLeague(FantasyLeagueDTO fantasyLeague) {
        this.fantasyLeague = fantasyLeague;
    }
}
