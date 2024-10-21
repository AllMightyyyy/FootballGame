package com.example.Player.services;

import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.TransferList;
import com.example.Player.models.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    @Autowired
    private TransferListRepository transferListRepository;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    public void listPlayerForSale(FantasyTeam fantasyTeam, Long fantasyPlayerId, double price) throws Exception {
        FantasyPlayer fantasyPlayer = fantasyPlayerService.getFantasyPlayer(fantasyPlayerId)
                .orElseThrow(() -> new Exception("Fantasy Player not found."));

        if (!fantasyPlayer.getFantasyTeam().equals(fantasyTeam)) {
            throw new Exception("Player does not belong to your team.");
        }

        if (transferListRepository.findByFantasyPlayerAndStatus(fantasyPlayer, TransferStatus.LISTED).isPresent()) {
            throw new Exception("Player is already listed for sale.");
        }

        TransferList transferList = new TransferList();
        transferList.setFantasyPlayer(fantasyPlayer);
        transferList.setListedPrice(price);
        transferList.setStatus(TransferStatus.LISTED);
        transferListRepository.save(transferList);
    }

    public void purchasePlayer(FantasyTeam buyerTeam, Long transferListId) throws Exception {
        TransferList transferList = transferListRepository.findById(transferListId)
                .orElseThrow(() -> new Exception("Transfer listing not found."));

        if (transferList.getStatus() != TransferStatus.LISTED) {
            throw new Exception("Player is not available for purchase.");
        }

        FantasyPlayer fantasyPlayer = transferList.getFantasyPlayer();
        double price = transferList.getListedPrice();

        if (buyerTeam.getBalance() < price) {
            throw new Exception("Insufficient balance to purchase this player.");
        }

        // Deduct buyer's balance
        buyerTeam.setBalance(buyerTeam.getBalance() - price);
        fantasyTeamService.saveFantasyTeam(buyerTeam);

        // Transfer player
        FantasyTeam sellerTeam = fantasyPlayer.getFantasyTeam();
        fantasyPlayer.setFantasyTeam(buyerTeam);
        fantasyPlayer.setAssigned(true);
        fantasyPlayerService.saveFantasyPlayer(fantasyPlayer);

        // Add balance to seller's team
        sellerTeam.setBalance(sellerTeam.getBalance() + price);
        fantasyTeamService.saveFantasyTeam(sellerTeam);

        // Update transfer listing status
        transferList.setStatus(TransferStatus.SOLD);
        transferListRepository.save(transferList);

        // Record transaction
        transactionService.recordTransfer(buyerTeam, sellerTeam, fantasyPlayer, price);
    }

    // Additional methods like canceling listings, fetching transfer lists, etc.
}
