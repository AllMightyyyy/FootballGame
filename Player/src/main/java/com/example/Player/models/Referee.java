package com.example.Player.models;

import jakarta.persistence.*;

@Entity
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private RefereeLeniency leniency;

    public Referee() {
    }

    public Referee(Long id, String name, RefereeLeniency leniency) {
        this.id = id;
        this.name = name;
        this.leniency = leniency;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RefereeLeniency getType() {
        return leniency;
    }

    public void setType(RefereeLeniency type) {
        this.leniency = type;
    }
}
