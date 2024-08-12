package org.simple.bankingsystem.events;

import org.simple.bankingsystem.entities.Transaction;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

public class OnTransactionSavedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Transaction transaction;
    private final Boolean successfullySaved;

    public OnTransactionSavedEvent(Transaction transaction, Boolean successfullySaved) {
        super(transaction);
        this.transaction = transaction;
        this.successfullySaved = successfullySaved;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Boolean getSuccessfullySaved() {
        return successfullySaved;
    }
}