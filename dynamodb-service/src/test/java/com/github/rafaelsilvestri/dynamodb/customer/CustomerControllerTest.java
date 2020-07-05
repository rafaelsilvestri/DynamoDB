package com.github.rafaelsilvestri.dynamodb.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;

/**
 * Endpoints test class using testcontainers to run the tests.
 *
 * @author Rafael Silvestri
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = {CustomerControllerTest.ApplicationInitializer.class})
class CustomerControllerTest {

    private static final int DYNAMODB_PORT = 8000;

    @Autowired
    DynamoDbAsyncClient dynamoDbAsyncClient;

    @Container
    static GenericContainer container =
            new GenericContainer<>("amazon/dynamodb-local")
                    .withExposedPorts(DYNAMODB_PORT);

    static class ApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext ctx) {
            TestPropertyValues.of(
                    String.format("amazon.dynamodb.endpoint=http://%s:%s",
                            container.getContainerIpAddress(), container.getMappedPort(DYNAMODB_PORT)))
                    .applyTo(ctx);
        }
    }

    @Autowired
    public WebTestClient webTestClient;

    //@BeforeEach
    void beforeEach() {
        CompletableFuture<CreateTableResponse> createTable = dynamoDbAsyncClient.createTable(CreateTableRequest.builder()
                .tableName("customer")
                .attributeDefinitions(AttributeDefinition.builder().attributeName("id").attributeType("S").build())
                .keySchema(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build());

        Mono.fromFuture(createTable).block();
    }

    @Test
    void shouldCreateCustomerWhenPostInvoked() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Rafael");
        customer.setLastName("Silvestri");
        customer.setEmail("rafaelcechinel@gmail.com");

        webTestClient
                .post()
                .uri("/v1/customers")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().value("Location", is(not(blankOrNullString())));
    }
}