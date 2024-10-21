// FantasyMatchSimulationRequest.java
package com.example.Player.DTO;

import com.example.Player.models.Referee;
import com.example.Player.models.Tactics;
import com.example.Player.models.League;
import com.example.Player.models.User;

public class FantasyMatchSimulationRequest {
    private League realLeague;
    private User user1;
    private User user2;
    private Tactics tactics1;
    private Tactics tactics2;
    private Referee referee;

    // Getters and Setters

    public League getRealLeague() {
        return realLeague;
    }

    public void setRealLeague(League realLeague) {
        this.realLeague = realLeague;
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

    public Tactics getTactics1() {
        return tactics1;
    }

    public void setTactics1(Tactics tactics1) {
        this.tactics1 = tactics1;
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
