package org.simple.bankingsystem.exceptions;

public class MissingCustomerAccountsException extends Exception{

    private final String message = "Customer accounts are missing.";

    public MissingCustomerAccountsException(){
    }

    @Override
    public String getMessage(){
        return message;
    }
}
