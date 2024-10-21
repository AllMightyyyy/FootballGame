package com.example.Player.models;

import jakarta.persistence.*;

@Entity
@Table(name = "transfer_list")
public class TransferList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fantasy_player_id", nullable = false)
    private FantasyPlayer fantasyPlayer;

    private double listedPrice;

    @Enumerated(EnumType.STRING)
    private TransferStatus status; // e.g., LISTED, SOLD, CANCELLED

    // Getters and Setters
}
