package com.example.Player.utils;

import java.util.List;

public class ScoreDTO {
    private List<Integer> ht; // Half-time score
    private List<Integer> ft; // Full-time score

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
