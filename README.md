# Webflux + Amazon DynamoDB

This is a simple POC using reactive programing and Amazon DynamoDB.

## Technologies
* [Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
* [Amazon DynamoDB](https://aws.amazon.com/dynamodb/)
* [Docker](https://www.docker.com/)
* [Testcontainers](https://www.testcontainers.org/)

## Getting Started
This project uses a container to run DynamoDB locally. You can start the stack by typing the command 
bellow:
```bash
docker-compose up -d
```
Stop the stack:
```bash
docker-compose down
```

## Amazon DynamoDB through the CLI
There are a few scripts that simulate a simple CRUD for a Person entity.

[01-create-table.sh](01-create-table.sh)  
[02-put-item.sh](02-put-item.sh)  
[03-get-item.sh](03-get-item.sh)  
[04-put-item-with-condition.sh](04-put-item-with-condition.sh)  
[05-update-item.sh](05-update-item.sh)   
[06-update-item-add-fields.sh](06-update-item-add-fields.sh)   
[07-update-item-with-condition.sh](07-update-item-with-condition.sh)   
[08-delete-item.sh](08-delete-item.sh)

### Running through the browser
You can also access the DynamoDB shell to execute commands and find many templates to start using.

Open http://localhost:8042/shell/ and start palying

# API - dynamodb-service

This is a simple CRUD endpoints to play with Amazon DynamoDB and reactive stack webflux.

### Testing

Creates a new entry
```
curl -d '{"id":"e8721b36-ac73-47df-b044-f318ee8b6f20","firstName":"Rafael","lastName":"Silvestri","email":"rafaelcechinel@gmail.com"}' \
    -H "Content-Type: application/json" \
    -X POST http://localhost:8080/v1/customers
```

Performs a full scan on `customer` table
```
curl -H "Content-Type: application/json" -X GET http://localhost:8080/v1/customers |jq
```

Retrieves a simple item from `customer` table
```
curl -H "Content-Type: application/json" -X GET http://localhost:8080/v1/customers/e8721b36-ac73-47df-b044-f318ee8b6f20 |jq
```

## References

[Amazon DynamoDB](https://aws.amazon.com/dynamodb)  
[AWS SDK for Java v2](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/examples-dynamodb.html)