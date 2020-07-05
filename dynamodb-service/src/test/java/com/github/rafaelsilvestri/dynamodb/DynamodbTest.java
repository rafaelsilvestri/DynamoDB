package com.github.rafaelsilvestri.dynamodb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

/**
 * Basic commands test class.
 *
 * @author Rafael Silvestri
 */
class DynamodbTest {

    private static DynamoDbClient client;

    @BeforeAll
    static void setUp() {
        // Create the DynamoDbClient object
        Region region = Region.US_EAST_1;
        client = DynamoDbClient.builder()
                .region(region)
                .endpointOverride(URI.create("http://localhost:8042/"))
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return "dynamodb";
                    }

                    @Override
                    public String secretAccessKey() {
                        return "dynamodbsecretkey";
                    }
                })
                .build();
    }

    @Test
    void shouldCreateTableIfNotExists() {
        // delete table if exists
        String tableName = "TestTable";
        if (exists(tableName)) {
            delete(tableName);
        }

        // Create the CreateTableRequest object
        final String key = "MY_TABLE_KEY";
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(key)
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName(key)
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
                .tableName(tableName)
                .build();

        // Execute table creation
        try {
            CreateTableResponse response = client.createTable(request);
            String result = response.tableDescription().tableName();
            Assertions.assertEquals(tableName, result);
        } catch (DynamoDbException e) {
            Assertions.fail(e.getMessage());
        }
    }

    private void delete(String tableName) {
        client.deleteTable(builder -> builder.tableName(tableName));
    }

    private boolean exists(String tableName) {
        ListTablesResponse response = client.listTables();
        return response.tableNames()
                .stream()
                .anyMatch(tableName::equalsIgnoreCase);

    }
}
