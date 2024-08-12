package org.simple.bankingsystem.services;

import org.simple.bankingsystem.entities.Account;
import org.simple.bankingsystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void updateAccounts(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }

    public List<Account> getAccounts(Set<Long> accountIds) {
       return accountRepository.findAllInIds(accountIds);
    }
}
