package com.example.Player.DTO;

import com.example.Player.models.Referee;
import com.example.Player.models.Tactics;
import com.example.Player.models.User;

public class FantasyMatchSimulationRequest {
    private String realLeagueCode;
    private User user1;
    private User user2;
    private Tactics tactics1;
    private Tactics tactics2;
    private Referee referee;

    public FantasyMatchSimulationRequest() {
    }

    public FantasyMatchSimulationRequest(String realLeagueCode, User user2, User user1, Tactics tactics1, Tactics tactics2, Referee referee) {
        this.realLeagueCode = realLeagueCode;
        this.user2 = user2;
        this.user1 = user1;
        this.tactics1 = tactics1;
        this.tactics2 = tactics2;
        this.referee = referee;
    }

    // Getters and Setters

    public String getRealLeagueCode() {
        return realLeagueCode;
    }

    public void setRealLeagueCode(String realLeagueCode) {
        this.realLeagueCode = realLeagueCode;
    }

    public Tactics getTactics1() {
        return tactics1;
    }

    public void setTactics1(Tactics tactics1) {
        this.tactics1 = tactics1;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Tactics getTactics2() {
        return tactics2;
    }

    public void setTactics2(Tactics tactics2) {
        this.tactics2 = tactics2;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }
}

