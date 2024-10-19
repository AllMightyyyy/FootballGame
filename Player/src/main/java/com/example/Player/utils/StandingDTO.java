// src/main/java/com/example/Player/utils/StandingDTO.java

package com.example.Player.utils;

public class StandingDTO {
    private String teamName;
    private int played;
    private int win;
    private int draw;
    private int loss; // Corrected field name
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points;

    // Constructors
    public StandingDTO() {}

    public StandingDTO(String teamName) {
        this.teamName = teamName;
        this.played = 0;
        this.win = 0;
        this.draw = 0;
        this.loss = 0; // Initialize loss
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.goalDifference = 0;
        this.points = 0;
    }

    // Getters and Setters

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLoss() { // Corrected method name
        return loss;
    }

    public void setLoss(int loss) { // Corrected method name
        this.loss = loss;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
