package org.simple.bankingsystem.services;

import org.simple.bankingsystem.api.messages.Response;
import org.simple.bankingsystem.api.messages.TransactionRequest;
import org.simple.bankingsystem.api.messages.TransactionHistoryResponse;
import org.simple.bankingsystem.entities.Transaction;
import org.simple.bankingsystem.exceptions.InvalidCustomerIdException;
import org.simple.bankingsystem.exceptions.MissingCustomerAccountsException;
import org.simple.bankingsystem.exceptions.TransactionFailedException;
import org.simple.bankingsystem.filters.TransactionFilter;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    /** Saves transaction to the database.
     * @param transaction Transaction object which needs to be added to the database.
     * @return Transaction ID
     * @throws TransactionFailedException If the insertion failed, the exception is thrown.
     */
    Long saveTransaction(Transaction transaction) throws TransactionFailedException;

    /** Saves the transaction using the received TransactionRequest.
     * @param transactionRequest Transaction request which needs to be mapped to the
     *                           Transaction object in order to be saved.
     * @return Response which has transaction ID if the insertion was successful, otherwise it contains an error message.
     */
    Response saveTransaction(TransactionRequest transactionRequest) ;

    /** Gets transaction history of the customer filtered by transaction values if they exist.
     * @param filter An object which has fields that match Transaction fields and if any of the fields has a value,
     *               a filtering of the customer transactions will be done with those fields.
     * @return A list of transactions of the customer.
     * @throws InvalidCustomerIdException Thrown if customer ID has incorrect value (0 or less) or if a customer
     * with ID does not exist.
     * @throws MissingCustomerAccountsException Thrown if no accounts are found for the customer.
     */
    List<Transaction> getCustomerTransactionHistory(TransactionFilter filter) throws InvalidCustomerIdException,
            MissingCustomerAccountsException;


    /** Used for mapping parameters for filtering when transaction history is called.
     * @param customerId Customer ID
     * @param transactionId Transaction ID
     * @param receiverAccountId Receiver Account ID
     * @param senderAccountId Sender accound ID
     * @param amount Amount of the transaction
     * @param currencyId Currency ID
     * @param description Description
     * @param timeStamp Timestamp
     * @return Transaction filter which has all the fields which were sent populated.
     */
    TransactionFilter mapParametersToTransactionFilter(
            Long customerId,
            Long transactionId,
            Long receiverAccountId,
            Long senderAccountId,
            Double amount,
            Integer currencyId,
            String description,
            LocalDateTime timeStamp);


    /**
     * @return List of transactions made in the last month.
     */
    List<Transaction> getTransactionInThePastMonth();

    /**
     * @param filter Used for filtering the fetched transactions.
     * @return Transaction list mapped to transaction history response.
     */
     TransactionHistoryResponse getTransactionHistory(TransactionFilter filter);

    /**
     * @param transactions List of transactions to be saved.
     */
    void saveTransactions(List<Transaction> transactions);
}
