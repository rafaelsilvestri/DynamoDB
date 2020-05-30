# Overview

A placeholder to play around with the local DynamoDB instance.

### Technologies
* [Amazon DynamoDB](https://aws.amazon.com/dynamodb/)
* [Docker](https://www.docker.com/)

### Starting the stack
```bash
docker-compose up -d
```
### Stop the stack
```bash
docker-compose down
```

### Running through the command line
There are a few scripts that simulate a simple CRUD for a Product entity.

[01-create-table.sh](01-create-table.sh)  
[02-put-item.sh](02-put-item.sh)  
[03-get-item.sh](03-get-item.sh)  
[04-put-item-with-condition.sh](04-put-item-with-condition.sh)  
[05-update-item.sh](05-update-item.sh)   
[06-update-item-add-fields.sh](06-update-item-add-fields.sh)   
[07-update-item-with-condition.sh](07-update-item-with-condition.sh)   
[08-delete-item.sh](08-delete-item.sh)


## References
