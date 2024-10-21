package com.example.Player.models;

public class Tactics {
    private String gamePlan; // e.g., "Long Balls", "Possession Game"
    private TacklingIntensity tacklingIntensity; // NORMAL, AGGRESSIVE, PASSIVE
    private int pressing; // 0 to 100
    private String style; // e.g., "Park the Bus", "Defensive", "Neutral", "Attacking", "All Out Attack"
    private MarkingType markingType; // ZONAL, MAN_TO_MAN
    private boolean offsideTrap;

    public Tactics() {
    }

    public Tactics(String gamePlan, String style, TacklingIntensity tacklingIntensity, int pressing, MarkingType markingType, boolean offsideTrap) {
        this.gamePlan = gamePlan;
        this.style = style;
        this.tacklingIntensity = tacklingIntensity;
        this.pressing = pressing;
        this.markingType = markingType;
        this.offsideTrap = offsideTrap;
    }

    // Getters and Setters

    public String getGamePlan() {
        return gamePlan;
    }

    public void setGamePlan(String gamePlan) {
        this.gamePlan = gamePlan;
    }

    public TacklingIntensity getTacklingIntensity() {
        return tacklingIntensity;
    }

    public void setTacklingIntensity(TacklingIntensity tacklingIntensity) {
        this.tacklingIntensity = tacklingIntensity;
    }

    public int getPressing() {
        return pressing;
    }

    public void setPressing(int pressing) {
        this.pressing = pressing;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public MarkingType getMarkingType() {
        return markingType;
    }

    public void setMarkingType(MarkingType markingType) {
        this.markingType = markingType;
    }

    public boolean isOffsideTrap() {
        return offsideTrap;
    }

    public void setOffsideTrap(boolean offsideTrap) {
        this.offsideTrap = offsideTrap;
    }
}
