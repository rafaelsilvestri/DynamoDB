#!/bin/bash

aws dynamodb delete-item \
	--endpoint-url http://localhost:8042 \
	--table-name person-info \
  	--key '{"personId": {"S": "999"}}' \
  	--return-values ALL_OLD
