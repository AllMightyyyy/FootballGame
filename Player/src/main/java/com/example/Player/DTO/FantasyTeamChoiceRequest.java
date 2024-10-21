package com.example.Player.DTO;

import com.example.Player.models.League;

public class FantasyTeamChoiceRequest {
    private String teamName;
    private String realLeagueCode;

    public FantasyTeamChoiceRequest() {
    }

    public FantasyTeamChoiceRequest(String teamName, String realLeagueCode) {
        this.teamName = teamName;
        this.realLeagueCode = realLeagueCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getRealLeagueCode() {
        return realLeagueCode;
    }

    public void setRealLeagueCode(String realLeagueCode) {
        this.realLeagueCode = realLeagueCode;
    }

    public League getRealLeague() {
        // TODO -> return real league League object out of the code or name idk
        return new League();
    }
}
