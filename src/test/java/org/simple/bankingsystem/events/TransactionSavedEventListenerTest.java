package org.simple.bankingsystem.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.simple.bankingsystem.email.EmailSenderService;
import org.simple.bankingsystem.entities.Account;
import org.simple.bankingsystem.entities.Transaction;
import org.simple.bankingsystem.services.AccountService;
import org.simple.bankingsystem.services.CustomerService;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionSavedEventListenerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AccountService accountService;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private TransactionSavedEventListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onApplicationEvent_withSuccessfulTransaction_sendsNotification() {
        Long senderAccountId = 1L;
        Long receiverAccountId = 2L;
        Double amount = 100.0;
        Integer currencyId = 759;

        Transaction transaction = new Transaction(
                123L,
                senderAccountId,
                receiverAccountId,
                amount,
                currencyId,
                "",
                LocalDateTime.now());

        Account senderAccount = new Account(senderAccountId,"12345", "ABC");
        senderAccount.setBalance(1000.0);

        Account receiverAccount = new Account(receiverAccountId, "6789", "DEF");
        receiverAccount.setBalance(500.0);

        when(accountService.getAccounts(anySet())).thenReturn(Arrays.asList(senderAccount, receiverAccount));
        when(customerService.getCustomerEmailByAccountId(anySet())).thenReturn(Map.of(
                1L, "sender@example.com",
                2L, "receiver@example.com"
        ));

        OnTransactionSavedEvent event = new OnTransactionSavedEvent(transaction, true);

        listener.onApplicationEvent(event);

        // Verify accounts updated
        verify(accountService, times(1)).updateAccounts(anyList());
        // Verify emails sent
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailSenderService, times(2))
                .sendSimpleEmail(emailCaptor.capture(),
                eq("Transaction Notification"), messageCaptor.capture());

        List<String> emails = emailCaptor.getAllValues();
        assertTrue(emails.contains("sender@example.com"));
        assertTrue(emails.contains("receiver@example.com"));
    }

    @Test
    void onApplicationEvent_withUnsuccessfulTransaction_sendsFailureNotification() {
        Long senderAccountId = 1L;
        Long receiverAccountId = 2L;
        Double amount = 100.0;
        Integer currencyId = 759;

        Transaction transaction = new Transaction(
                123L,
                senderAccountId,
                receiverAccountId,
                amount,
                currencyId,
                "",
                LocalDateTime.now());

        when(customerService.getCustomerEmailByAccountId(anySet()))
                .thenReturn(Map.of(
                1L, "sender@example.com",
                2L, "receiver@example.com"
                ));

        OnTransactionSavedEvent event = new OnTransactionSavedEvent(transaction, false);

        listener.onApplicationEvent(event);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(accountService, never()).updateAccounts(anyList());
        verify(emailSenderService, times(2))
                .sendSimpleEmail(emailCaptor.capture(),
                        eq("Transaction Notification"), messageCaptor.capture());
        List<String> emails = emailCaptor.getAllValues();
        assertTrue(emails.contains("sender@example.com"));
        assertTrue(emails.contains("receiver@example.com"));
        List<String> messages = messageCaptor.getAllValues();
        assertTrue(messages.contains("Transaction with amount 100,000000 was unsuccessfully processed"));
    }
}