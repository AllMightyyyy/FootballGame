package com.example.Player.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {

    @Id
    private Long id; // sofifa_id

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
    private int heightCm;
    private int weightKg;
    private String leagueName;
    private String clubName;
    private String nationalityName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "player_positions", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "position")
    private List<String> positionsList;

    // Player attributes
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

    public Player(double valueEur, Long id, String shortName, String longName, String positions, int overall, int potential, double wageEur, String playerFaceUrl, String clubLogoUrl, String nationFlagUrl, int heightCm, int weightKg, String leagueName, String clubName, String nationalityName, int pace, int shooting, int passing, int dribbling, int defending, int physical) {
        this.valueEur = valueEur;
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
        this.positions = positions;
        this.overall = overall;
        this.potential = potential;
        this.wageEur = wageEur;
        this.playerFaceUrl = playerFaceUrl;
        this.clubLogoUrl = clubLogoUrl;
        this.nationFlagUrl = nationFlagUrl;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.leagueName = leagueName;
        this.clubName = clubName;
        this.nationalityName = nationalityName;
        this.pace = pace;
        this.shooting = shooting;
        this.passing = passing;
        this.dribbling = dribbling;
        this.defending = defending;
        this.physical = physical;
    }

    public Player(Long id, double valueEur, String shortName, String longName, String positions, int overall, int potential, double wageEur, String playerFaceUrl, String clubLogoUrl, String nationFlagUrl, int heightCm, int weightKg, String leagueName, String clubName, String nationalityName, List<String> positionsList, int pace, int shooting, int passing, int dribbling, int defending, int physical) {
        this.id = id;
        this.valueEur = valueEur;
        this.shortName = shortName;
        this.longName = longName;
        this.positions = positions;
        this.overall = overall;
        this.potential = potential;
        this.wageEur = wageEur;
        this.playerFaceUrl = playerFaceUrl;
        this.clubLogoUrl = clubLogoUrl;
        this.nationFlagUrl = nationFlagUrl;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.leagueName = leagueName;
        this.clubName = clubName;
        this.nationalityName = nationalityName;
        this.positionsList = positionsList;
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

    public int getHeightCm() { return heightCm; }
    public void setHeightCm(int heightCm) { this.heightCm = heightCm; }

    public int getWeightKg() { return weightKg; }
    public void setWeightKg(int weightKg) { this.weightKg = weightKg; }

    public String getLeagueName() { return leagueName; }
    public void setLeagueName(String leagueName) { this.leagueName = leagueName; }

    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }

    public String getNationalityName() { return nationalityName; }
    public void setNationalityName(String nationalityName) { this.nationalityName = nationalityName; }

    public List<String> getPositionsList() { return positionsList; }
    public void setPositionsList(List<String> positionsList) { this.positionsList = positionsList; }
}

