package org.simple.bankingsystem.exceptions;

public class TransactionFailedException extends Exception{

    public TransactionFailedException(String message) {
        super(message);
    }
}
