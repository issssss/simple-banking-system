package org.simple.bankingsystem.api.messages;

import org.simple.bankingsystem.entities.Customer;
import org.springframework.http.HttpStatus;

public class CustomerResponse extends Response{
    private String name;
    private String email;
    private String phone;
    private String address;

    public CustomerResponse(String message, HttpStatus status) {
        super(message, status);
    }

    public CustomerResponse(Customer customer) {
        this.setId(customer.getId());
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phone = customer.getPhoneNumber();
        this.address = customer.getAddress();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
