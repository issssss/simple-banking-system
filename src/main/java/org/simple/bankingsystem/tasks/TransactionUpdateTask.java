package org.simple.bankingsystem.tasks;

import org.simple.bankingsystem.SimpleBankingSystemApplication;
import org.simple.bankingsystem.entities.Account;
import org.simple.bankingsystem.entities.Transaction;
import org.simple.bankingsystem.services.AccountService;
import org.simple.bankingsystem.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TransactionUpdateTask {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final Logger log = LoggerFactory.getLogger(SimpleBankingSystemApplication.class);

    @Autowired
    public TransactionUpdateTask(TransactionService transactionService, AccountService accountService)
    {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @Scheduled(cron = "${transaction.job.cron}")
    @Async
    public void calculatePastMonthTurnover()
    {
        List<Transaction> transactionsInPastMonth = transactionService.getTransactionInThePastMonth();
        if (transactionsInPastMonth.isEmpty())
        {
            log.info("No transactions in past month");
            return;
        }
        Set<Long> accountIds = transactionsInPastMonth.stream().map(Transaction::getReceiverAccountId).collect(Collectors.toSet());
        accountIds.addAll(transactionsInPastMonth.stream().map(Transaction::getSenderAccountId).collect(Collectors.toSet()));
        List<Account> allAccountsToUpdate = accountService.getAccounts(accountIds);
        allAccountsToUpdate.forEach(account -> account.setPastMonthTurnover(0.0));
        if (allAccountsToUpdate.isEmpty())
        {
            log.error("No accounts found to update");
        }

        log.info("Updating accounts for past period transactions");
        for (Transaction transaction : transactionsInPastMonth)
        {
            Optional<Account> receiverAccount = allAccountsToUpdate.stream()
                    .filter(account -> transaction.getReceiverAccountId().equals(account.getId()))
                    .findFirst();
            if (receiverAccount.isEmpty()) {
                log.error("Receiver account {} not found", transaction.getReceiverAccountId());
            }
            else
            {
                Double receiverAccountPastTurnover = receiverAccount.get().getPastMonthTurnover();
                receiverAccountPastTurnover += transaction.getAmount();
                receiverAccount.get().setPastMonthTurnover(receiverAccountPastTurnover);
            }
            Optional<Account> senderAccount = allAccountsToUpdate.stream()
                    .filter(account -> transaction.getSenderAccountId().equals(account.getId()))
                    .findFirst();
            if (senderAccount.isEmpty()) {
                log.error("Sender account {} not found", transaction.getSenderAccountId());
            }
            else
            {
                Double senderAccountPastTurnover = senderAccount.get().getPastMonthTurnover();
                senderAccountPastTurnover -= transaction.getAmount();
                senderAccount.get().setPastMonthTurnover(senderAccountPastTurnover);
            }
        }
        accountService.updateAccounts(allAccountsToUpdate);
        log.info("Updating accounts for past period transactions finished.");
    }
}
