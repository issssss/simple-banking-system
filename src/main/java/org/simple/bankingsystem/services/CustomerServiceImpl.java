package org.simple.bankingsystem.services;

import org.simple.bankingsystem.api.messages.CustomerResponse;
import org.simple.bankingsystem.entities.Account;
import org.simple.bankingsystem.entities.Customer;
import org.simple.bankingsystem.exceptions.InvalidCustomerIdException;
import org.simple.bankingsystem.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findById(Long id) throws InvalidCustomerIdException {
        if (id <= 0)
        {
            throw new InvalidCustomerIdException(id);
        }
       return customerRepository.findById(id).orElseThrow(() -> new InvalidCustomerIdException(id));
    }

    public CustomerResponse findCustomerById(Long id) {
        try {
            Customer customer = findById(id);
            CustomerResponse customerResponse = new CustomerResponse(customer);
            customerResponse.setMessage("Success");
            customerResponse.setStatus(HttpStatus.OK);
            return customerResponse;
        }
        catch (Exception e) {
            return new CustomerResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Set<Long> getCustomerAccountIds(Long customerId) throws InvalidCustomerIdException {
        if (customerId <= 0){
            throw new InvalidCustomerIdException(customerId);
        }
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()){
            throw new InvalidCustomerIdException(customerId);
        }

        List<Account> accounts = customer.get().getAccounts();
        if (accounts.isEmpty()) {
            return new HashSet<>();
        }
        Set<Long> accountIds = accounts.stream().map(Account::getId).collect(Collectors.toSet());
        return accountIds;
    }

    public Map<Long, String> getCustomerEmailByAccountId(Set<Long> accountIds) {
        if (accountIds.isEmpty()){
            throw new InvalidParameterException("Account Ids must not be null or empty");
        }

        Map<Long, String> customerByAccountId = new HashMap<>();
        for (Long accountId : accountIds) {
            String email = customerRepository.findCustomerByAccountId(accountId);
            customerByAccountId.put(accountId, email);
        }
        return customerByAccountId;
    }

    public void saveCustomers(List<Customer> customers)
    {
        customerRepository.saveAll(customers);
    }
}
