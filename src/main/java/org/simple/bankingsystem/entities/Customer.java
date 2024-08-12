package org.simple.bankingsystem.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;

    @OneToMany(fetch =  FetchType.EAGER, mappedBy = "customer", cascade = CascadeType.MERGE)
    private List<Account> accounts;

    protected Customer() {}

    public Customer(String name, String address, String email, String phoneNumber, List<Account> accounts) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accounts = accounts;
    }

    public Customer(String name, String address, String email, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(((Customer) obj).id, this.id);
    }

    @Override
    public String toString() {
        return String.format("%d - %s", id, name);
    }

}
