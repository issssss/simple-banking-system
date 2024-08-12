package org.simple.bankingsystem.api.controllers;

import org.simple.bankingsystem.api.messages.Response;
import org.simple.bankingsystem.api.messages.TransactionRequest;
import org.simple.bankingsystem.api.messages.TransactionHistoryResponse;
import org.simple.bankingsystem.filters.TransactionFilter;
import org.simple.bankingsystem.services.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Class for Transaction REST API
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * @param transactionRequest The transaction request that needs to be inserted into the database.
     * @return Response which contains ID of the transaction if it was successful, otherwise an error message.
     */
    @PutMapping("")
    public Response addTransaction(@RequestBody TransactionRequest transactionRequest) {
        Response response = transactionService.saveTransaction(transactionRequest);
        return response;
    }

    /**
     * @param id Customer ID
     * @param transactionId Optional parameter, used for fetching a specific transaction by its ID
     * @param receiverAccountId Optional parameter, used for filtering the transactions by receiver account ID
     * @param senderAccountId Optional parameter, used for filtering the transactions by sender account ID
     * @param amount Optional parameter, used for filtering the transactions amount
     * @param currencyId Optional parameter, used for filtering the transactions by currency ID
     * @param description Optional parameter, used for filtering the transactions by description ID
     * @param timestamp Optional parameter, used for filtering the transactions by timestamp
     * @return A response containing the list of transaction, if the operation was successful.
     */
    @GetMapping("/history/{id}")
    public TransactionHistoryResponse getTransactionHistoryByCustomerId(
            @PathVariable Long id,
            @RequestParam Optional<Long> transactionId,
            @RequestParam Optional<Long> receiverAccountId,
            @RequestParam Optional<Long> senderAccountId,
            @RequestParam Optional<Double> amount,
            @RequestParam Optional<Integer> currencyId,
            @RequestParam Optional<String> description,
            @RequestParam Optional<LocalDateTime> timestamp)
    {
        TransactionFilter transactionFilter = transactionService.mapParametersToTransactionFilter(
                id,
                transactionId.orElse(null),
                receiverAccountId.orElse(null),
                senderAccountId.orElse(null),
                amount.orElse(null),
                currencyId.orElse(null),
                description.orElse(null),
                timestamp.orElse(null));

        return transactionService.getTransactionHistory(transactionFilter);
    }
}
