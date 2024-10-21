package com.example.Player.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_listings")
public class TransferListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fantasy_player_id", nullable = false)
    private FantasyPlayer fantasyPlayer;

    @ManyToOne
    @JoinColumn(name = "fantasy_team_id", nullable = false)
    private FantasyTeam fantasyTeam;

    private double price;

    @Enumerated(EnumType.STRING)
    private TransferStatus status; // LISTED, SOLD, CANCELLED

    private LocalDateTime listedAt;

    public TransferListing() {
    }

    public TransferListing(Long id, FantasyPlayer fantasyPlayer, FantasyTeam fantasyTeam, double price, TransferStatus status, LocalDateTime listedAt) {
        this.id = id;
        this.fantasyPlayer = fantasyPlayer;
        this.fantasyTeam = fantasyTeam;
        this.price = price;
        this.status = status;
        this.listedAt = listedAt;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FantasyPlayer getFantasyPlayer() {
        return fantasyPlayer;
    }

    public void setFantasyPlayer(FantasyPlayer fantasyPlayer) {
        this.fantasyPlayer = fantasyPlayer;
    }

    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public LocalDateTime getListedAt() {
        return listedAt;
    }

    public void setListedAt(LocalDateTime listedAt) {
        this.listedAt = listedAt;
    }
}
