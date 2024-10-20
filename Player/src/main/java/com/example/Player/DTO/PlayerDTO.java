package com.example.Player.DTO;

import java.util.List;

public class PlayerDTO {

    private Long id;                // sofifa_id
    private String shortName;
    private String longName;
    private String positions;       // Original positions (as a string)
    private List<String> positionsList;  // List of positions
    private int overall;
    private int potential;
    private double valueEur;
    private double wageEur;

    private String playerFaceUrl;   // URL to the player's image
    private String clubLogoUrl;     // URL to the club logo
    private String nationFlagUrl;   // URL to the nationality flag

    private String clubName;        // The club name
    private String nationalityName; // Nationality name
    private int heightCm;
    private int weightKg;

    // Player stats
    private int pace;
    private int shooting;
    private int passing;
    private int dribbling;
    private int defending;
    private int physical;

    private int goalkeepingDiving;
    private int goalkeepingHandling;
    private int goalkeepingKicking;
    private int goalkeepingPositioning;
    private int goalkeepingReflexes;
    private int goalkeepingSpeed;

    private String leagueName;      // The league name the player is in
    private String leagueCode;      // League code (e.g., "fr.1")

    // Getters and setters for each field

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

    public List<String> getPositionsList() {
        return positionsList;
    }

    public void setPositionsList(List<String> positionsList) {
        this.positionsList = positionsList;
    }

    public int getOverall() {
        return overall;
    }

    public void setOverall(int overall) {
        this.overall = overall;
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

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public int getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(int heightCm) {
        this.heightCm = heightCm;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(int weightKg) {
        this.weightKg = weightKg;
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

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
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

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueCode() {
        return leagueCode;
    }

    public void setLeagueCode(String leagueCode) {
        this.leagueCode = leagueCode;
    }

    public int getGoalkeepingHandling() {
        return goalkeepingHandling;
    }

    public void setGoalkeepingHandling(int goalkeepingHandling) {
        this.goalkeepingHandling = goalkeepingHandling;
    }

    public int getGoalkeepingDiving() {
        return goalkeepingDiving;
    }

    public void setGoalkeepingDiving(int goalkeepingDiving) {
        this.goalkeepingDiving = goalkeepingDiving;
    }

    public int getGoalkeepingKicking() {
        return goalkeepingKicking;
    }

    public void setGoalkeepingKicking(int goalkeepingKicking) {
        this.goalkeepingKicking = goalkeepingKicking;
    }

    public int getGoalkeepingPositioning() {
        return goalkeepingPositioning;
    }

    public void setGoalkeepingPositioning(int goalkeepingPositioning) {
        this.goalkeepingPositioning = goalkeepingPositioning;
    }

    public int getGoalkeepingReflexes() {
        return goalkeepingReflexes;
    }

    public void setGoalkeepingReflexes(int goalkeepingReflexes) {
        this.goalkeepingReflexes = goalkeepingReflexes;
    }

    public int getGoalkeepingSpeed() {
        return goalkeepingSpeed;
    }

    public void setGoalkeepingSpeed(int goalkeepingSpeed) {
        this.goalkeepingSpeed = goalkeepingSpeed;
    }
}
