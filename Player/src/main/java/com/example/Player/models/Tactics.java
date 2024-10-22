// Tactics.java
package com.example.Player.models;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
public class Tactics {
    private String gamePlan; // e.g., "Long Balls", "Possession Game"
    private TacklingIntensity tacklingIntensity; // NORMAL, AGGRESSIVE, PASSIVE
    private int pressing; // 0 to 100
    private String style; // e.g., "Park the Bus", "Defensive", "Neutral", "Attacking", "All Out Attack"
    private MarkingType markingType; // ZONAL, MAN_TO_MAN
    private boolean offsideTrap;

    // Constructors
    public Tactics() {
    }

    public Tactics(String gamePlan, TacklingIntensity tacklingIntensity, int pressing, String style, MarkingType markingType, boolean offsideTrap) {
        this.gamePlan = gamePlan;
        this.tacklingIntensity = tacklingIntensity;
        this.pressing = pressing;
        this.style = style;
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

    /**
     * Converts gamePlan to a numerical value for simulation impact.
     */
    public double getGamePlanValue() {
        switch (gamePlan.toUpperCase()) {
            case "POSSESSION GAME":
                return 1.1;
            case "LONG BALL":
                return 0.9;
            default:
                return 1.0;
        }
    }

    /**
     * Converts tacklingIntensity to a factor.
     */
    public double getTacklingIntensityFactor() {
        switch (tacklingIntensity) {
            case AGGRESSIVE:
                return 1.2;
            case PASSIVE:
                return 0.8;
            default:
                return 1.0;
        }
    }

    /**
     * Converts style to a numerical value for simulation impact.
     */
    public double getStyleValue() {
        switch (style.toUpperCase()) {
            case "PARK THE BUS":
                return 0.5;
            case "DEFENSIVE":
                return 0.7;
            case "ATTACKING":
                return 1.3;
            case "ALL OUT ATTACK":
                return 1.5;
            default:
                return 1.0;
        }
    }

    /**
     * Converts markingType to a numerical value for simulation impact.
     */
    public double getMarkingTypeValue() {
        switch (markingType) {
            case MAN_TO_MAN:
                return 1.0;
            case ZONAL:
                return 1.1;
            default:
                return 1.0;
        }
    }
}
