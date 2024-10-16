package com.example.Player.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Player {
    private Long id;
    private String shortName;
    private String longName;
    private String positions;
    private int overall;
    private int potential;
    private double valueEur;
    private double wageEur;
    private String playerFaceUrl;
    private String clubLogoUrl;
    private String nationFlagUrl;

    // New fields for player attributes
    private int pace;
    private int shooting;
    private int passing;
    private int dribbling;
    private int defending;
    private int physical;

    public Player() {
    }

    public Player(String longName, Long id, String shortName, String positions, int overall, int potential, double valueEur, double wageEur, String playerFaceUrl, String clubLogoUrl, String nationFlagUrl, int pace, int shooting, int passing, int dribbling, int defending, int physical) {
        this.longName = longName;
        this.id = id;
        this.shortName = shortName;
        this.positions = positions;
        this.overall = overall;
        this.potential = potential;
        this.valueEur = valueEur;
        this.wageEur = wageEur;
        this.playerFaceUrl = playerFaceUrl;
        this.clubLogoUrl = clubLogoUrl;
        this.nationFlagUrl = nationFlagUrl;
        this.pace = pace;
        this.shooting = shooting;
        this.passing = passing;
        this.dribbling = dribbling;
        this.defending = defending;
        this.physical = physical;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public int getShooting() {
        return shooting;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public int getDribbling() {
        return dribbling;
    }

    public void setDribbling(int dribbling) {
        this.dribbling = dribbling;
    }

    public int getDefending() {
        return defending;
    }

    public void setDefending(int defending) {
        this.defending = defending;
    }

    public int getPhysical() {
        return physical;
    }

    public void setPhysical(int physical) {
        this.physical = physical;
    }

    public int getOverall() {
        return overall;
    }

    public void setOverall(int overall) {
        this.overall = overall;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public int getPotential() {
        return potential;
    }

    public void setPotential(int potential) {
        this.potential = potential;
    }

    public double getValueEur() {
        return valueEur;
    }

    public void setValueEur(double valueEur) {
        this.valueEur = valueEur;
    }

    public double getWageEur() {
        return wageEur;
    }

    public void setWageEur(double wageEur) {
        this.wageEur = wageEur;
    }

    public String getPlayerFaceUrl() {
        return playerFaceUrl;
    }

    public void setPlayerFaceUrl(String playerFaceUrl) {
        this.playerFaceUrl = playerFaceUrl;
    }

    public String getClubLogoUrl() {
        return clubLogoUrl;
    }

    public void setClubLogoUrl(String clubLogoUrl) {
        this.clubLogoUrl = clubLogoUrl;
    }

    public String getNationFlagUrl() {
        return nationFlagUrl;
    }

    public void setNationFlagUrl(String nationFlagUrl) {
        this.nationFlagUrl = nationFlagUrl;
    }
}

