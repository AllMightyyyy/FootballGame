// src/main/java/com/example/Player/utils/LeagueDTO.java

package com.example.Player.DTO;

import java.util.List;

public class LeagueDTO {
    private String name;
    private String code;
    private String season;
    private List<MatchDTO> matches; // List of matches
    private List<StandingDTO> standings;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season){
        this.season = season;
    }

    public List<MatchDTO> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchDTO> matches){
        this.matches = matches;
    }

    public List<StandingDTO> getStandings() {
        return standings;
    }

    public void setStandings(List<StandingDTO> standings){
        this.standings = standings;
    }
}
