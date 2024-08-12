package org.simple.bankingsystem.api.messages;

import org.simple.bankingsystem.entities.Transaction;

import java.time.LocalDateTime;

public class TransactionRequest extends Request{
    private Long senderAccountId;
    private Long receiverAccountId;
    private Double amount;
    private Integer currencyId;
    private String description;
    private LocalDateTime timestamp;

    public TransactionRequest() {
        super(null);
    }

    public TransactionRequest(Transaction transaction){
        super(transaction.getTransactionID());
        this.senderAccountId = transaction.getSenderAccountId();
        this.receiverAccountId = transaction.getReceiverAccountId();
        this.amount = transaction.getAmount();
        this.currencyId = transaction.getCurrencyId();
        this.description = transaction.getDescription();
        this.timestamp = transaction.getTimestamp();
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
