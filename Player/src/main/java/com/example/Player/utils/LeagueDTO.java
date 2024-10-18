package com.example.Player.utils;

import java.util.List;

public class LeagueDTO {
    private String name;
    private List<MatchDTO> matches;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MatchDTO> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchDTO> matches) {
        this.matches = matches;
    }
}
