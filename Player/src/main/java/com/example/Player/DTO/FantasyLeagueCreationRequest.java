package com.example.Player.DTO;

public class FantasyLeagueCreationRequest {
    private String realLeagueCode;
    private String fantasyLeagueName;

    public FantasyLeagueCreationRequest() {
    }

    public FantasyLeagueCreationRequest(String realLeagueCode, String fantasyLeagueName) {
        this.realLeagueCode = realLeagueCode;
        this.fantasyLeagueName = fantasyLeagueName;
    }

    public String getRealLeagueCode() {
        return realLeagueCode;
    }

    public void setRealLeagueCode(String realLeagueCode) {
        this.realLeagueCode = realLeagueCode;
    }

    public String getFantasyLeagueName() {
        return fantasyLeagueName;
    }

    public void setFantasyLeagueName(String fantasyLeagueName) {
        this.fantasyLeagueName = fantasyLeagueName;
    }
}
