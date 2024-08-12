package org.simple.bankingsystem.services;

import org.simple.bankingsystem.api.messages.Response;
import org.simple.bankingsystem.api.messages.TransactionRequest;
import org.simple.bankingsystem.api.messages.TransactionHistoryResponse;
import org.simple.bankingsystem.entities.Transaction;
import org.simple.bankingsystem.enums.InclusionEnum;
import org.simple.bankingsystem.events.OnTransactionSavedEvent;
import org.simple.bankingsystem.exceptions.InvalidCustomerIdException;
import org.simple.bankingsystem.exceptions.MissingCustomerAccountsException;
import org.simple.bankingsystem.exceptions.TransactionFailedException;
import org.simple.bankingsystem.filters.TransactionFilter;
import org.simple.bankingsystem.repositories.TransactionRepository;
import org.simple.bankingsystem.specifications.SearchCriteria;
import org.simple.bankingsystem.specifications.TransactionSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public TransactionServiceImpl(
            CustomerService customerService,
            TransactionRepository transactionRepository,
            ApplicationEventPublisher eventPublisher) {
        this.customerService = customerService;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    public Long saveTransaction(Transaction transaction) throws TransactionFailedException {
        boolean successfullySaved = false;
        try {
            transactionRepository.save(transaction);
            successfullySaved = true;
        }
        catch (Exception e) {
            throw new TransactionFailedException(e.getMessage());
        }
        finally {
            eventPublisher.publishEvent(new OnTransactionSavedEvent(transaction, successfullySaved));
        }

        return transaction.getTransactionID();
    }

    public Response saveTransaction(TransactionRequest transactionRequest) {
        Response response = new Response();
        try {

            Transaction transaction = new Transaction(transactionRequest);
            Long transactionId = saveTransaction(transaction);
            response.setStatus(HttpStatus.CREATED);
            response.setId(transactionId);
            response.setMessage("Transaction added successfully");
        }
        catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public TransactionFilter mapParametersToTransactionFilter(
            Long customerId,
            Long transactionId,
            Long receiverAccountId,
            Long senderAccountId,
            Double amount,
            Integer currencyId,
            String description,
            LocalDateTime timeStamp) {
        if (customerId == null)
        {
            throw new InvalidParameterException("customerId is null");
        }

        TransactionFilter transactionFilter = new TransactionFilter();
        transactionFilter.customerId = customerId;
        transactionFilter.transactionId = transactionId;
        transactionFilter.receiverAccountId = receiverAccountId;
        transactionFilter.senderAccountId = senderAccountId;
        transactionFilter.amount = amount;
        transactionFilter.currencyId = currencyId;
        transactionFilter.description = description;
        transactionFilter.timeStamp = timeStamp;

        return transactionFilter;
    }

    public List<Transaction> getCustomerTransactionHistory(TransactionFilter filter)
            throws InvalidCustomerIdException, MissingCustomerAccountsException {
        if (filter.customerId <= 0){
            throw new InvalidCustomerIdException(filter.customerId);
        }

        Set<Long> customerAccountIds = customerService.getCustomerAccountIds(filter.customerId);
        if (customerAccountIds.isEmpty()){
            throw new MissingCustomerAccountsException();
        }

        if (filter.receiverAccountId != null
                && filter.senderAccountId != null
                && !customerAccountIds.contains(filter.senderAccountId)
                && !customerAccountIds.contains(filter.receiverAccountId)){
            return Collections.emptyList();
        }

        Specification<Transaction> transactionSpecification = createTransactionSpecification(filter, customerAccountIds);
        List<Transaction> customerTransactions = transactionRepository.findAll(transactionSpecification);
        customerTransactions.sort(Comparator.comparing(Transaction::getTimestamp));

        return customerTransactions;
    }

    public TransactionHistoryResponse getTransactionHistory(TransactionFilter filter) {
        try {
            List<Transaction> customerTransactions = getCustomerTransactionHistory(filter);
            List<TransactionRequest> transactions = new ArrayList<>();
            for (Transaction transaction : customerTransactions){
                transactions.add(new TransactionRequest(transaction));
            }
            TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse(
                    filter.customerId,
                    "Success",
                    HttpStatus.OK);
            transactionHistoryResponse.setTransactions(transactions);
            return transactionHistoryResponse;
        }
        catch (Exception e) {
            return new TransactionHistoryResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<Transaction> getTransactionInThePastMonth() {
        LocalDateTime monthBeforeDate = LocalDateTime.now().minusMonths(1);
        return transactionRepository.getTransactionsBeforeDate(monthBeforeDate);
    }

    public void saveTransactions(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    /**
     * @param filter Used for filtering the transaction history according to the optional parameters. Using the filers
     *               search criteria is made for the specification.
     * @param accountIds Account IDs which belong to the customer. Only used if neither receiver account ID nor sender
     *                   account ID are set in the filter.
     * @return Specification used for filtering when fetching the transactions from the database.
     */
    private Specification<Transaction> createTransactionSpecification(TransactionFilter filter, Set<Long> accountIds) {
        if (filter == null)
        {
            throw new InvalidParameterException("filter is null");
        }
        if (accountIds == null || accountIds.isEmpty())
        {
            throw new InvalidParameterException("accountIds is null or empty");
        }

        List<SearchCriteria> searchCriteria = new LinkedList<>();
        Specification<Transaction> allCustomerAccountsSpecification = null;
        if (filter.transactionId != null && filter.transactionId > 0)
        {
            searchCriteria.add(new SearchCriteria("id", filter.transactionId));
        }
        if (filter.receiverAccountId == null && filter.senderAccountId == null)
        {
            List<SearchCriteria> criteria = new LinkedList<>();

            criteria.add(new SearchCriteria("receiverAccountId", accountIds));
            criteria.add(new SearchCriteria("senderAccountId", accountIds, InclusionEnum.OR));
            allCustomerAccountsSpecification = new TransactionSpecificationBuilder(criteria).build();
        }
        if (filter.receiverAccountId != null
                && filter.receiverAccountId > 0) {
            searchCriteria.add(new SearchCriteria("receiverAccountId", filter.receiverAccountId));
        }
        if (filter.senderAccountId != null
                && filter.senderAccountId > 0) {
            searchCriteria.add(new SearchCriteria("senderAccountId", filter.senderAccountId));
        }
        if (filter.amount != null && filter.amount > 0)
        {
            searchCriteria.add(new SearchCriteria("amount", filter.amount));
        }
        if (filter.currencyId != null && filter.currencyId > 0)
        {
            searchCriteria.add(new SearchCriteria("currencyId", filter.currencyId));
        }
        if (filter.description != null && !filter.description.isBlank())
        {
            searchCriteria.add(new SearchCriteria("description", filter.description));
        }
        if (filter.timeStamp != null)
        {
            searchCriteria.add(new SearchCriteria("timestamp", filter.timeStamp));
        }

        TransactionSpecificationBuilder transactionSpecificationBuilder = new TransactionSpecificationBuilder(searchCriteria);
        Specification<Transaction> transactionSpecification = transactionSpecificationBuilder.build();
        if (allCustomerAccountsSpecification != null)
        {
            transactionSpecification = transactionSpecification.and(allCustomerAccountsSpecification);
        }
        return transactionSpecification;
    }
}
