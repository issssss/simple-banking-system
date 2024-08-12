package org.simple.bankingsystem.events;

import org.simple.bankingsystem.email.EmailSenderService;
import org.simple.bankingsystem.entities.Account;
import org.simple.bankingsystem.entities.Transaction;
import org.simple.bankingsystem.services.AccountService;
import org.simple.bankingsystem.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TransactionSavedEventListener implements ApplicationListener<OnTransactionSavedEvent> {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailSenderService emailSenderService;

    private final String subject = "Transaction Notification";
    private String templateMessage = """
              Hello!

              The transaction with ID: {id} has been processed successfully/unsuccessfully,
              and the balance: {balance} has been added/taken from your account with ID {accoundId}.
             \s
              Old balance: {old balance}
              New balance: {new balance}
             \s
              Regards,
              Your XYZ bank\
            """;

    @Override
    @Async
    public void onApplicationEvent(OnTransactionSavedEvent event) {
        Transaction transaction = event.getTransaction();
        if (transaction == null) {
            throw new IllegalArgumentException("transaction is null");
        }

        List<Account> accounts = new ArrayList<>();
        if (event.getSuccessfullySaved()) {
            Long senderAccountId = transaction.getSenderAccountId();
            Long receiverAccountId = transaction.getReceiverAccountId();
            Double amount = transaction.getAmount();
            accounts = updateAccountBalances(senderAccountId, receiverAccountId, amount);
        }
        sendTransactionNotification(accounts, transaction, event.getSuccessfullySaved());
    }

    /** Updates accounts' balances connected to the transaction.
     * @param senderAccountId Account ID from which the amount was sent.
     * @param receiverAccountId Account ID which received the amount.
     * @param amount The amount exchanged through the transaction.
     * @return List of accounts with the updated balance.
     */
    private List<Account> updateAccountBalances(Long senderAccountId, Long receiverAccountId, Double amount) {
        Set<Long> accountIds = new HashSet<>();
        accountIds.add(senderAccountId);
        accountIds.add(receiverAccountId);
        List<Account> accounts = accountService.getAccounts(accountIds);

        for (Account account : accounts) {
            double balance = account.getBalance();
            double newBalance = Objects.equals(account.getId(), senderAccountId)
                    ? balance - amount
                    : balance + amount;
            account.setBalance(newBalance);
        }
        accountService.updateAccounts(accounts);

        return accounts;
    }

    /** Sends emails to the customers between whom the transaction happened.
     * @param accounts Accounts of the customers.
     * @param transaction Transaction.
     * @param successfulTransaction Indicates if the transaction was successfully saved or if it failed.
     */
    private void sendTransactionNotification(List<Account> accounts, Transaction transaction, boolean successfulTransaction) {

        Set<Long> accountIds = new HashSet<>();
        accountIds.add(transaction.getSenderAccountId());
        accountIds.add(transaction.getReceiverAccountId());
        Map<Long,String> emailByAccountId = customerService.getCustomerEmailByAccountId(accountIds);

        if (!successfulTransaction) {
            String message = String.format("Transaction with amount %f was unsuccessfully processed", transaction.getAmount());
            for (String email : emailByAccountId.values()) {
                emailSenderService.sendSimpleEmail(email, subject, message);
            }
            return;
        }

        for (Account account : accounts) {
            double oldBalance = account.getBalance().doubleValue();
            if (Objects.equals(account.getId(), transaction.getSenderAccountId())) {
                oldBalance += transaction.getAmount();
            }
            else if (Objects.equals(account.getId(), transaction.getReceiverAccountId())) {
                oldBalance -= transaction.getAmount();
            }

            String email = emailByAccountId.get(account.getId());
            String message = templateMessage.replace("{id}", transaction.getTransactionID().toString())
                    .replace("{balance}", transaction.getAmount().toString())
                    .replace("{old balance}", String.format("%.11f", oldBalance))
                    .replace("{new balance}", account.getBalance().toString())
                    .replace("{accoundId}", account.getId().toString());

            emailSenderService.sendSimpleEmail(email, subject, message);
        }
    }
}
