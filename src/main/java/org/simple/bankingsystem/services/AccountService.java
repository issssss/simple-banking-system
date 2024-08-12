package org.simple.bankingsystem.services;

import org.simple.bankingsystem.entities.Account;

import java.util.List;
import java.util.Set;

public interface AccountService {
    /**
     * @param accounts Accounts which need to be saved or updated.
     */
    void updateAccounts(List<Account> accounts);

    /** Fetches a list of accounts which match with the passed IDs.
     * @param accountIds Sent account IDs
     * @return List of accounts.
     */
    List<Account> getAccounts(Set<Long> accountIds);
}
