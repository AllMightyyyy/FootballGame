package com.example.Player.DTO;

public class TransferPurchaseRequest {
    private Long transferListId;

    public TransferPurchaseRequest() {
    }

    public TransferPurchaseRequest(Long transferListId) {
        this.transferListId = transferListId;
    }

    // Getters and Setters

    public Long getTransferListId() {
        return transferListId;
    }

    public void setTransferListId(Long transferListId) {
        this.transferListId = transferListId;
    }
}

