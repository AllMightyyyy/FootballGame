package com.example.Player.repository;

import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.TransferList;
import com.example.Player.models.TransferListing;
import com.example.Player.models.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferListingRepository extends JpaRepository<TransferListing, Long> {
    List<TransferListing> findByStatus(TransferStatus status);
    Optional<TransferListing> findByFantasyPlayerAndStatus(FantasyPlayer fantasyPlayer, TransferStatus status);
}

