#!/bin/bash


aws dynamodb update-item \
	--endpoint-url http://localhost:8042 \
	--table-name person-info \
  	--key '{"personId": {"S": "999"}}' \
  	--update-expression 'SET #firstName = :firstName,  #lastName = :lastName, #dateOfBirth = :dateOfBirth' \
 	--expression-attribute-names '{"#firstName": "firstName", "#lastName": "lastName", "#dateOfBirth": "dateOfBirth"}' \
  	--expression-attribute-values '{":firstName": {"S":"Rafael"}, ":lastName": {"S": "Silvestri"}, ":dateOfBirth": {"S": "1984-08-20"}}' \
  	--return-values ALL_NEW
