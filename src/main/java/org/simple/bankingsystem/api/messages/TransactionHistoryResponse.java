package org.simple.bankingsystem.api.messages;

import org.springframework.http.HttpStatus;
import java.util.List;

public class TransactionHistoryResponse extends Response{
   private List<TransactionRequest> transactions;

    public TransactionHistoryResponse( String message, HttpStatus status) {
        super(message, status);
    }

    public TransactionHistoryResponse(Long id, String message, HttpStatus status) {
        super(id, message, status);
    }

    public List<TransactionRequest> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionRequest> transactions) {
        this.transactions = transactions;
    }
}
