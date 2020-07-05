package com.github.rafaelsilvestri.dynamodb.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Business rules class.
 *
 * @author Rafael Silvestri
 */
@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void save(Customer customer) throws JsonProcessingException {
        customerRepository.save(customer);
    }

    public Customer getById(UUID id) {
        return customerRepository.getById(id);
    }

    public List<Customer> getAll() {
        return customerRepository.getAll();
    }
}
