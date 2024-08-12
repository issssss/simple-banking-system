package org.simple.bankingsystem.fileimport.parsers;

import org.simple.bankingsystem.entities.Transaction;
import org.simple.bankingsystem.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class TransactionFileParser extends FileParser {

    private final TransactionService transactionService;

    @Autowired
    public TransactionFileParser(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Async
    @Override
    CompletableFuture<Void> mapAndSave(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(separator);
            Transaction transaction = new Transaction(
                    Long.parseLong(parts[0]),
                    Long.parseLong(parts[1]),
                    Double.parseDouble(parts[2]),
                    Integer.parseInt(parts[3]),
                    parts[4],
                    LocalDateTime.parse(parts[5]));
            transactions.add(transaction);
        }
        transactionService.saveTransactions(transactions);

        return CompletableFuture.completedFuture(null);
    }
}
