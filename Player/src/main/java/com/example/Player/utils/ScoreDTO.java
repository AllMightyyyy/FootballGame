// src/main/java/com/example/Player/utils/ScoreDTO.java

package com.example.Player.utils;

import java.util.List;

public class ScoreDTO {
    private List<Integer> ht; // Half-time scores: [team1, team2]
    private List<Integer> ft; // Full-time scores: [team1, team2]

    // Getters and Setters

    public List<Integer> getHt() {
        return ht;
    }

    public void setHt(List<Integer> ht) {
        this.ht = ht;
    }

    public List<Integer> getFt() {
        return ft;
    }

    public void setFt(List<Integer> ft) {
        this.ft = ft;
    }
}
