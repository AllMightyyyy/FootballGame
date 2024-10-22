/*
package com.example.Player.services;

import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.TransferListing;
import com.example.Player.models.TransferStatus;
import com.example.Player.repository.TransferListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferListingService {

    @Autowired
    private TransferListingRepository transferListingRepository;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    @Autowired
    private TransactionService transactionService;

    public void listPlayerForTransfer(FantasyTeam fantasyTeam, Long fantasyPlayerId, double price) throws Exception {
        FantasyPlayer fantasyPlayer = fantasyPlayerService.getFantasyPlayer(fantasyPlayerId)
                .orElseThrow(() -> new Exception("Fantasy Player not found."));

        if (!fantasyPlayer.getFantasyTeam().equals(fantasyTeam)) {
            throw new Exception("Player does not belong to your team.");
        }

        if (transferListingRepository.findByFantasyPlayerAndStatus(fantasyPlayer, TransferStatus.LISTED).isPresent()) {
            throw new Exception("Player is already listed for transfer.");
        }

        TransferListing listing = new TransferListing();
        listing.setFantasyPlayer(fantasyPlayer);
        listing.setListingTeam(fantasyTeam);
        listing.setPrice(price);
        listing.setStatus(TransferStatus.LISTED);
        listing.setListedAt(LocalDateTime.now());

        transferListingRepository.save(listing);
    }

    public void purchasePlayer(Long transferListingId, FantasyTeam buyerTeam) throws Exception {
        TransferListing listing = transferListingRepository.findById(transferListingId)
                .orElseThrow(() -> new Exception("Transfer Listing not found."));

        if (listing.getStatus() != TransferStatus.LISTED) {
            throw new Exception("Transfer Listing is not available.");
        }

        FantasyPlayer fantasyPlayer = listing.getFantasyPlayer();
        FantasyTeam sellerTeam = listing.getListingTeam();
        double price = listing.getPrice();

        if (buyerTeam.getBalance() < price) {
            throw new Exception("Insufficient balance to purchase this player.");
        }

        // Transfer logic
        buyerTeam.setBalance(buyerTeam.getBalance() - price);
        sellerTeam.setBalance(sellerTeam.getBalance() + price);
        fantasyTeamService.saveFantasyTeam(buyerTeam);
        fantasyTeamService.saveFantasyTeam(sellerTeam);

        fantasyPlayer.setFantasyTeam(buyerTeam);
        fantasyPlayer.setAssigned(true);
        fantasyPlayerService.saveFantasyPlayer(fantasyPlayer);

        // Update listing status
        listing.setStatus(TransferStatus.SOLD);
        transferListingRepository.save(listing);

        // Record transaction
        transactionService.recordTransfer(sellerTeam, buyerTeam, fantasyPlayer, price);
    }

    // Additional methods like canceling listings, fetching listings, etc.
}
*/