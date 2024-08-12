package org.simple.bankingsystem.fileimport.parsers;

import org.simple.bankingsystem.entities.Customer;
import org.simple.bankingsystem.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class CustomerFileParser extends FileParser {

    private final CustomerService customerService;
    @Autowired
    public CustomerFileParser(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Async
    @Override
    CompletableFuture<Void> mapAndSave(List<String> lines) {
        List<Customer> customers = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(separator);
            Customer customer = new Customer(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3]);
            customers.add(customer);
        }
        customerService.saveCustomers(customers);
        return CompletableFuture.completedFuture(null);
    }
}
