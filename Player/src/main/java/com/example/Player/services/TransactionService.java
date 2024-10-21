package com.example.Player.services;

import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void recordTransfer(FantasyTeam sellerTeam, FantasyTeam buyerTeam, FantasyPlayer fantasyPlayer, double amount) {
        Transaction transaction = new Transaction();
        transaction.setType("TRANSFER");
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setFantasyTeam(buyerTeam);
        transaction.setFantasyPlayer(fantasyPlayer);
        transactionRepository.save(transaction);
    }

    // Existing transaction recording methods
}
