package com.example.Player.utils;

public class MatchDTO {
    private String round;
    private String date;
    private String time;
    private String team1;
    private String team2;
    private ScoreDTO score;

    // Getters and Setters

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public ScoreDTO getScore() {
        return score;
    }

    public void setScore(ScoreDTO score) {
        this.score = score;
    }
}
