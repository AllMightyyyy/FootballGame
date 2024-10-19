// src/main/java/com/example/Player/utils/MatchDTO.java

package com.example.Player.utils;

import com.example.Player.models.Match;
import org.mapstruct.Mapping;


public class MatchDTO {
    private String round;
    private String date;
    private String time;
    private String team1; // Team1 name
    private String team2; // Team2 name
    private ScoreDTO score;
    @Mapping(source = "team1.name", target = "team1")
    @Mapping(source = "team2.name", target = "team2")

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

    public void setDate(String date){
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

    public void setTeam1(String team1){
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2){
        this.team2 = team2;
    }

    public ScoreDTO getScore() {
        return score;
    }

    public void setScore(ScoreDTO score){
        this.score = score;
    }
}
