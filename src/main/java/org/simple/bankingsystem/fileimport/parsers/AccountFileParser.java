package org.simple.bankingsystem.fileimport.parsers;

import org.simple.bankingsystem.entities.Account;
import org.simple.bankingsystem.entities.Customer;
import org.simple.bankingsystem.exceptions.InvalidCustomerIdException;
import org.simple.bankingsystem.services.AccountService;
import org.simple.bankingsystem.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class AccountFileParser extends FileParser {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Autowired
    public AccountFileParser(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @Override
    CompletableFuture<Void> mapAndSave(List<String> lines) {
        List<Account> accounts = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(separator);
            Long customerId = Long.parseLong(parts[0]);

            try {
                Customer customer = customerService.findById(customerId);
                Account account = new Account(
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]));
                account.setCustomer(customer);
                accounts.add(account);
            } catch (InvalidCustomerIdException e) {
                log.error(e.getMessage());
            }
        }

        accountService.updateAccounts(accounts);

        return CompletableFuture.completedFuture(null);
    }
}
