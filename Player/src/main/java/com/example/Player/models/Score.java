package com.example.Player.models;

public class Score {
    private int[] ht; // Half-time score
    private int[] ft; // Full-time score

    public Score() {
    }

    public Score(int[] ht, int[] ft) {
        this.ht = ht;
        this.ft = ft;
    }

    public int[] getHt() {
        return ht;
    }

    public void setHt(int[] ht) {
        this.ht = ht;
    }

    public int[] getFt() {
        return ft;
    }

    public void setFt(int[] ft) {
        this.ft = ft;
    }
}
