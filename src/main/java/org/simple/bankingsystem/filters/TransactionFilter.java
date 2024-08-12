package org.simple.bankingsystem.filters;

import java.time.LocalDateTime;

public class TransactionFilter {
    public Long customerId;
    public Long transactionId;
    public Long receiverAccountId;
    public Long senderAccountId;
    public Double amount;
    public Integer currencyId;
    public String description;
    public LocalDateTime timeStamp;
}
