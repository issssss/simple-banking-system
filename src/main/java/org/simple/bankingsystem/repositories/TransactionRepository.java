package org.simple.bankingsystem.repositories;

import org.simple.bankingsystem.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    @Query(value="select t from #{#entityName} t where t.timestamp >= :date")
    List<Transaction> getTransactionsBeforeDate(LocalDateTime date);
}
