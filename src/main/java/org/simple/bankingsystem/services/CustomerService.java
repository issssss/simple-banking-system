package org.simple.bankingsystem.services;

import org.simple.bankingsystem.api.messages.CustomerResponse;
import org.simple.bankingsystem.entities.Customer;
import org.simple.bankingsystem.exceptions.InvalidCustomerIdException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomerService {

    /** Finds a customer according to the passed customer ID.
     * @param id Customer ID.
     * @return Customer.
     * @throws InvalidCustomerIdException If customer does not exist or the ID is invalid an error is thrown.
     */
    Customer findById(Long id) throws InvalidCustomerIdException;

    /** Finds a customer according to the passed customer ID and maps the customer to the Customer Response.
     * If the customer does not exist or an error occured, an error message will be shown.
     * @param id Customer ID.
     * @return Customer.
     */
    CustomerResponse findCustomerById(Long id);

    /** Gets account IDs. of the customer.
     * @param customerId Customer ID.
     * @return A list of account IDs.
     * @throws InvalidCustomerIdException If the ID is incorrect or the customer does not exist, exception in thrown.
     */
    Set<Long> getCustomerAccountIds(Long customerId) throws InvalidCustomerIdException;

    /** Gets customers' emails by the account IDs and maps them accordingly.
     * @param accountIds A set of account IDs.
     * @return A map where the account ID is value and customer, to whom the account belongs to, email.
     */
    Map<Long, String> getCustomerEmailByAccountId(Set<Long> accountIds);

    /** Saves a batch of customers to the database.
     * @param customers A list of customers.
     */
    void saveCustomers(List<Customer> customers);
}
