#!/bin/bash

aws dynamodb --endpoint-url http://localhost:8042 \
	create-table --table-name person-info \
	--attribute-definitions AttributeName=personId,AttributeType=S \
	--key-schema AttributeName=personId,KeyType=HASH \
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
#        "TableArn": "arn:aws:dynamodb:ddblocal:000000000000:table/person-info", 
#        "AttributeDefinitions": [
#            {
#                "AttributeName": "personId", 
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
#        "TableName": "person-info", 
#        "TableStatus": "ACTIVE", 
#        "KeySchema": [
#            {
#                "KeyType": "HASH", 
#                "AttributeName": "personId"
#            }
#        ], 
#        "ItemCount": 0, 
#        "CreationDateTime": 1590856227.739
#    }
#}

