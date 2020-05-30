#!/bin/bash

aws dynamodb --endpoint-url http://localhost:8042 \
	create-table --table-name product-info \
	--attribute-definitions AttributeName=productId,AttributeType=S \
	--key-schema AttributeName=productId,KeyType=HASH \
	--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5



##################################################################################
#### Documentation
##################################################################################
# endpoint-url: address exposed to access DynamoDB inside the Docker container.
# table-name: this is the name of the table that will be created.
# attribute-definitions: this is the description of the attribute that forms the table’s key schema. It has 2 parameters, the AttributeName and the AttributeType.
# key-schema: this is where we can define the attribute that is the primary key of the table.
# provisioned-throughput: this defines the throughput capacities (read and write) of our table.

#### expected output

# {
#    "TableDescription": {
#        "TableArn": "arn:aws:dynamodb:ddblocal:000000000000:table/product-info", 
#        "AttributeDefinitions": [
#            {
#                "AttributeName": "productId", 
#                "AttributeType": "S"
#            }
#        ], 
#        "ProvisionedThroughput": {
#            "NumberOfDecreasesToday": 0, 
#            "WriteCapacityUnits": 5, 
#            "LastIncreaseDateTime": 0.0, 
#            "ReadCapacityUnits": 5, 
#            "LastDecreaseDateTime": 0.0
#        }, 
#        "TableSizeBytes": 0, 
#        "TableName": "product-info", 
#        "TableStatus": "ACTIVE", 
#        "KeySchema": [
#            {
#                "KeyType": "HASH", 
#                "AttributeName": "productId"
#            }
#        ], 
#        "ItemCount": 0, 
#        "CreationDateTime": 1590856227.739
#    }
#}

