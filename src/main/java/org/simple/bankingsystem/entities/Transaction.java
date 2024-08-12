package org.simple.bankingsystem.entities;

import jakarta.persistence.*;
import org.simple.bankingsystem.api.messages.TransactionRequest;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long senderAccountId;
    private Long receiverAccountId;
    private Double amount;
    private Integer currencyId;
    private String description;
    private LocalDateTime timestamp;

    protected Transaction(){}

    public Transaction(Long senderAccountId,
                       Long receiverAccountId,
                       Double amount,
                       Integer currencyId,
                       String description,
                       LocalDateTime timestamp){
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.currencyId = currencyId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Transaction(
            Long id,
            Long senderAccountId,
            Long receiverAccountId,
            Double amount,
            Integer currencyId,
            String description,
            LocalDateTime timestamp){
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.currencyId = currencyId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Transaction(TransactionRequest transactionRequest){
        this.senderAccountId = transactionRequest.getSenderAccountId();
        this.receiverAccountId = transactionRequest.getReceiverAccountId();
        this.amount = transactionRequest.getAmount();
        this.currencyId = transactionRequest.getCurrencyId();
        this.description = transactionRequest.getDescription();
        this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return  timestamp;
    }

    public Long getTransactionID(){
        return id;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public String getDescription() {
        return description;
    }
}
