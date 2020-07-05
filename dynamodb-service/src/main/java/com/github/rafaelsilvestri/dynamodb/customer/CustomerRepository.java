package com.github.rafaelsilvestri.dynamodb.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private final DynamoDbClient client;

    public CustomerRepository(DynamoDbClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * Creating table on startup if not exists
     */
    @PostConstruct
    public void createTableIfNeeded() {
        boolean exists = client.listTables()
                .tableNames()
                .stream()
                .anyMatch(TABLE_NAME::equalsIgnoreCase);

        if (!exists)
            createTable();
        else
            LOGGER.info("Table " + TABLE_NAME + " already exists. Skipping table creation");
    }

    /**
     * Adds a new item to the customer table.
     *
     * @param customer entity
     * @throws JsonProcessingException
     */
    public void save(Customer customer) throws JsonProcessingException {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(ID_COLUMN, AttributeValue.builder().s(customer.getId().toString()).build());
        item.put(BODY_COLUMN, AttributeValue.builder().s(mapper.writeValueAsString(customer)).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        client.putItem(putItemRequest);
    }

    /**
     * Gets a single item from customer table.
     *
     * @param id customer's id
     * @return customer entity.
     */
    public Customer getById(UUID id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(ID_COLUMN, AttributeValue.builder().s(id.toString()).build());

        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .attributesToGet(BODY_COLUMN)
                .build();

        GetItemResponse getItemResponse = client.getItem(getRequest);
        if (!getItemResponse.hasItem()) {
            return null;
        }
        Map<String, AttributeValue> itemAttr = getItemResponse.item();
        String body = itemAttr.get(BODY_COLUMN).s();
        try {
            return mapper.readValue(body, Customer.class);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Performs a full scan on customer table.
     *
     * @return a collection of customers
     */
    public List<Customer> getAll() {
        return client.scan(builder -> builder.tableName(TABLE_NAME))
                .items()
                .stream()
                .map(map -> {
                    try {
                        return mapper.readValue(map.get(BODY_COLUMN).s(), Customer.class);
                    } catch (JsonProcessingException e) {
                        LOGGER.error(e.getMessage(), e);
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * helper method to create a new table.
     */
    private void createTable() {
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

        client.createTable(request);
    }
}
