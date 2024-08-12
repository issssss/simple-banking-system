package org.simple.bankingsystem.exceptions;

public class InvalidCustomerIdException extends Exception{
    private String message = "Invalid Customer ID {id}";
    private final Long customerId;

    public InvalidCustomerIdException(Long customerId) {
        super();
        this.customerId = customerId;
    }

    @Override
    public String getMessage() {
        message = message.replace("{id}", customerId.toString());
        return message;
    }
}
