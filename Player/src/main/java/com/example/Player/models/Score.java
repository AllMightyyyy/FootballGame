// src/main/java/com/example/Player/models/Score.java

package com.example.Player.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Score {
    private Integer htTeam1; // Half-time score for Team1
    private Integer htTeam2; // Half-time score for Team2
    private Integer ftTeam1; // Full-time score for Team1
    private Integer ftTeam2; // Full-time score for Team2

    // Constructors
    public Score() {}

    public Score(Integer htTeam1, Integer htTeam2, Integer ftTeam1, Integer ftTeam2) {
        this.htTeam1 = htTeam1;
        this.htTeam2 = htTeam2;
        this.ftTeam1 = ftTeam1;
        this.ftTeam2 = ftTeam2;
    }

    // Getters and Setters
    public Integer getHtTeam1() {
        return htTeam1;
    }

    public void setHtTeam1(Integer htTeam1) {
        this.htTeam1 = htTeam1;
    }

    public Integer getHtTeam2() {
        return htTeam2;
    }

    public void setHtTeam2(Integer htTeam2) {
        this.htTeam2 = htTeam2;
    }

    public Integer getFtTeam1() {
        return ftTeam1;
    }

    public void setFtTeam1(Integer ftTeam1) {
        this.ftTeam1 = ftTeam1;
    }

    public Integer getFtTeam2() {
        return ftTeam2;
    }

    public void setFtTeam2(Integer ftTeam2) {
        this.ftTeam2 = ftTeam2;
    }
}
