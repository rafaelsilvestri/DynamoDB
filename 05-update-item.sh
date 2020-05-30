#!/bin/bash

aws dynamodb update-item \
	--endpoint-url http://localhost:8042 \
	--table-name product-info \
  	--key '{"productId": {"S": "999"}}' \
  	--update-expression 'SET #email = :newEmail' \
 	--expression-attribute-names '{"#email": "email"}' \
 	--expression-attribute-values '{":newEmail": {"S": "rafaelcechinel@changed.com"}}'

