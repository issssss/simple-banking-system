package org.simple.bankingsystem.repositories;

import org.simple.bankingsystem.entities.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface AccountRepository extends CrudRepository<Account, Long> {
    @Query(value="select a from #{#entityName} a where a.id in :ids")
    List<Account> findAllInIds(Set<Long> ids);
}
