package com.github.rafaelsilvestri.dynamodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Value("${amazon.aws.accesskey}")
    String accessKey;

    @Value("${amazon.aws.secretkey}")
    String secretKey;

    @Value("${amazon.dynamodb.endpoint}")
    String dynamoEndpoint;

    @Bean
    AwsBasicCredentials awsBasicCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    @Bean
    DynamoDbAsyncClient dynamoDbClient(AwsBasicCredentials awsBasicCredentials) {
        DynamoDbAsyncClientBuilder clientBuilder = DynamoDbAsyncClient.builder();
        clientBuilder
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials));
        if (!dynamoEndpoint.isEmpty()) {
            clientBuilder.endpointOverride(URI.create(dynamoEndpoint));
        }
        return clientBuilder.build();
    }
}
