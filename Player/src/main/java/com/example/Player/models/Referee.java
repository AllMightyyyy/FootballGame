// Referee.java
package com.example.Player.models;

import jakarta.persistence.*;

@Entity
@Table(name = "referees")
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private RefereeLeniency leniency;

    // Constructors, Getters, and Setters

    public Referee() {
    }

    public Referee(Long id, String name, RefereeLeniency leniency) {
        this.id = id;
        this.name = name;
        this.leniency = leniency;
    }

    // Getters and Setters...

    /**
     * Converts leniency to a numerical factor for simulation impact.
     */
    public double getLeniencyFactor() {
        switch (leniency) {
            case LENIENT:
                return 0.8;
            case STRICT:
                return 1.2;
            default:
                return 1.0;
        }
    }
}
