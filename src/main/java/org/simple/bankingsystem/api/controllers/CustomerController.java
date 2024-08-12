package org.simple.bankingsystem.api.controllers;

import org.simple.bankingsystem.api.messages.CustomerResponse;
import org.simple.bankingsystem.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }
}
