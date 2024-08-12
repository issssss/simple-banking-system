package org.simple.bankingsystem.entities;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountNumber;
    private String accountType;
    private Double balance = 0.0;
    private Double pastMonthTurnover = 0.0;

    @ManyToOne
    private Customer customer;

    protected Account() {}

    public Account(Long id, String accountNumber, String accountType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }
    public Account(String accountNumber, String accountType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public Account(String accountNumber, String accountType, Double balance, Double pastMonthTurnover) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.pastMonthTurnover = pastMonthTurnover;
    }

    public Long getId() {
        return id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getPastMonthTurnover() {
        return pastMonthTurnover;
    }

    public void setPastMonthTurnover(Double pastMonthTurnover) {
        this.pastMonthTurnover = pastMonthTurnover;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return this.accountNumber;
    }
}
