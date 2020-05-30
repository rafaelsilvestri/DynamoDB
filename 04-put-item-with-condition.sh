#!/bin/bash

aws dynamodb put-item \
	--endpoint-url http://localhost:8042 \
	--table-name person-info \
  	--item '{"personId": {"S": "999"}, "email": {"S": "rafaelcechinel@gmail.com"}}' \
  	--condition-expression "attribute_not_exists(personId)"

### and error is expected since the put-item.sh allready created a person with id 999
### The condition-expression argument can also be used for update-item and delete-item operations.
