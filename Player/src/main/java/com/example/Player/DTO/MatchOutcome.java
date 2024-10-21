// MatchOutcome.java
package com.example.Player.DTO;

import java.util.ArrayList;
import java.util.List;

public class MatchOutcome {
    private int team1Score;
    private int team2Score;
    private List<String> events; // e.g., goals, cards, substitutions

    public MatchOutcome(int team1Score, int team2Score) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.events = new ArrayList<>();
    }

    // Getters and Setters

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public List<String> getEvents() {
        return events;
    }

    public void addEvent(String event) {
        this.events.add(event);
    }
}
