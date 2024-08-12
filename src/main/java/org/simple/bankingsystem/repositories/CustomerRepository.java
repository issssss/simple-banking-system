package org.simple.bankingsystem.repositories;
import org.simple.bankingsystem.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query(value="select c.email from #{#entityName} c inner join c.accounts a where a.id = :id")
    String findCustomerByAccountId(Long id);
}
