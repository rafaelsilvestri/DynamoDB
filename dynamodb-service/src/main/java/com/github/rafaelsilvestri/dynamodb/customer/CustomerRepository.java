package com.github.rafaelsilvestri.dynamodb.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Domain Repository
 *
 * @author Rafael Silvestri
 */
@Repository
public class CustomerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepository.class);

    private static final String TABLE_NAME = "customer";
    private static final String ID_COLUMN = "id";
    private static final String BODY_COLUMN = "body";

    private final ObjectMapper mapper;
    private final DynamoDbAsyncClient client;

    public CustomerRepository(DynamoDbAsyncClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * Creating table on startup if not exists
     */
    @PostConstruct
    public void createTableIfNeeded() throws ExecutionException, InterruptedException {
        CompletableFuture<CreateTableResponse> createTableResponse = client.listTables()
                .thenCompose(response -> {
                    boolean exists = response.tableNames().contains(TABLE_NAME);
                    if (!exists) {
                        return createTable();
                    } else {
                        LOGGER.info("Table " + TABLE_NAME + " already exists. Skipping table creation");
                        return CompletableFuture.completedFuture(null);
                    }
                });

        //Wait in synchronous manner for table creation
        createTableResponse.get();
    }

    /**
     * Adds or Update an item to the customer table.
     *
     * @param customer entity
     * @throws JsonProcessingException
     */
    public Mono<Customer> save(Customer customer) throws JsonProcessingException {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(ID_COLUMN, AttributeValue.builder().s(customer.getId().toString()).build());
        item.put(BODY_COLUMN, AttributeValue.builder().s(mapper.writeValueAsString(customer)).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        return Mono.fromCompletionStage(client.putItem(putItemRequest))
                .map(putItemResponse -> putItemResponse.attributes())
                .map(attributeValueMap -> customer);
    }

    /**
     * Delete a single customer
     *
     * @param id customer id
     * @return Void
     */
    public Mono<Void> delete(UUID id) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(ID_COLUMN, AttributeValue.builder().s(id.toString()).build());
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(item)
                .build();

        return Mono.fromCompletionStage(client.deleteItem(deleteItemRequest))
                .then();
    }

    /**
     * Gets a single item from customer table.
     *
     * @param id customer's id
     * @return customer entity.
     */
    public Mono<Customer> getById(UUID id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(ID_COLUMN, AttributeValue.builder().s(id.toString()).build());

        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .attributesToGet(BODY_COLUMN)
                .build();

        return Mono.fromCompletionStage(client.getItem(getRequest))
                .map(GetItemResponse::item)
                .map(item -> {
                    try {
                        Customer c = mapper.readValue(item.get(BODY_COLUMN).s(), Customer.class);
                   return c;
                    } catch (JsonProcessingException e) {
                        LOGGER.error(e.getMessage(), e);
                        return null;
                    }
                });
    }

    /**
     * Performs a full scan on customer table.
     *
     * @return a collection of customers
     */
    public Flux<Customer> getAll() {
        ScanRequest scanRequest = ScanRequest
                .builder()
                .tableName(TABLE_NAME)
                .build();

        return Mono.fromCompletionStage(client.scan(scanRequest))
                .map(ScanResponse::items)
                .map(items -> items
                        .stream()
                        .map(item -> {
                            try {
                                return mapper.readValue(item.get(BODY_COLUMN).s(), Customer.class);
                            } catch (JsonProcessingException e) {
                                LOGGER.error(e.getMessage(), e);
                                return null;
                            }
                        })
                        .collect(Collectors.toList()))
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * helper method to create a new table.
     */
    private CompletableFuture<CreateTableResponse> createTable() {
        LOGGER.info("Creating table " + TABLE_NAME);
        KeySchemaElement keySchemaElement = KeySchemaElement
                .builder()
                .attributeName(ID_COLUMN)
                .keyType(KeyType.HASH)
                .build();

        AttributeDefinition attributeDefinition = AttributeDefinition
                .builder()
                .attributeName(ID_COLUMN)
                .attributeType(ScalarAttributeType.S)
                .build();

        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(TABLE_NAME)
                .keySchema(keySchemaElement)
                .attributeDefinitions(attributeDefinition)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();

        return client.createTable(request);
    }
}
