package com.example.Player.models;

import java.util.List;

public class League {
    private String name;
    private List<Match> matches;

    public League(String name, List<Match> matches) {
        this.name = name;
        this.matches = matches;
    }

    public League() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
