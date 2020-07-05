package com.github.rafaelsilvestri.dynamodb.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<Customer> save(Customer customer) throws JsonProcessingException {
        return customerRepository.save(customer);
    }

    public Mono<Void> delete(UUID id) {
        return customerRepository.delete(id);
    }

    public Mono<Customer> getById(UUID id) {
        return customerRepository.getById(id);
    }

    public Flux<Customer> getAll() {
        return customerRepository.getAll();
    }
}
