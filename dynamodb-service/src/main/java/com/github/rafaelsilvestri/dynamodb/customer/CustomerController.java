package com.github.rafaelsilvestri.dynamodb.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

/**
 * Customer endpoints.
 *
 * @author Rafael Silvestri
 */
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveEvent(@RequestBody Customer customer) throws JsonProcessingException {
        customerService.save(customer);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Customer> getById(@PathVariable("id") UUID id, final ServerWebExchange exchange) {
        return Mono.fromCallable(() -> customerService.getById(id))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Customer>> getAll(final ServerWebExchange exchange) {
        return Mono.fromCallable(() -> customerService.getAll())
                .subscribeOn(Schedulers.boundedElastic());
    }

}
