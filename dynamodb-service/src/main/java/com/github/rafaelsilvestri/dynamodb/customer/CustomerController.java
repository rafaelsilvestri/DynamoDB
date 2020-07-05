package com.github.rafaelsilvestri.dynamodb.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Mono<ResponseEntity> save(@RequestBody Customer customer) throws JsonProcessingException {
        return customerService.save(customer)
                .map(result -> ResponseEntity.created(URI.create("/v1/customers/" + result.getId())).build());
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> delete(@PathVariable("id") UUID id) {
        return customerService.delete(id)
                .map(result -> ResponseEntity.noContent().build());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> getById(@PathVariable("id") UUID id) {
        return customerService.getById(id)
                .map(result -> ResponseEntity.ok(result));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> getAll() {
        return customerService.getAll()
                .collect(Collectors.toList())
                .map(result -> ResponseEntity.ok(result));
    }

}
