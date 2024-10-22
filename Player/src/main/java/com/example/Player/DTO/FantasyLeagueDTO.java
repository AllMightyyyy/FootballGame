package com.example.Player.DTO;

import java.util.List;

public class FantasyLeagueDTO {
    private Long id;
    private String name;
    private LeagueDTO realLeague;
    private List<TeamDTO> fantasyTeams;
    // Assuming you have FantasyPlayerDTO, otherwise omit or define it accordingly
    private List<FantasyPlayerDTO> availablePlayers;

    // Constructors
    public FantasyLeagueDTO() {}

    public FantasyLeagueDTO(Long id, String name, LeagueDTO realLeague, List<TeamDTO> fantasyTeams) {
        this.id = id;
        this.name = name;
        this.realLeague = realLeague;
        this.fantasyTeams = fantasyTeams;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LeagueDTO getRealLeague() {
        return realLeague;
    }

    public List<TeamDTO> getFantasyTeams() {
        return fantasyTeams;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRealLeague(LeagueDTO realLeague){
        this.realLeague = realLeague;
    }

    public void setFantasyTeams(List<TeamDTO> fantasyTeams){
        this.fantasyTeams = fantasyTeams;
    }


    public List<FantasyPlayerDTO> getAvailablePlayers() {
        return availablePlayers;
    }

    public void setAvailablePlayers(List<FantasyPlayerDTO> availablePlayers) {
        this.availablePlayers = availablePlayers;
    }
}
