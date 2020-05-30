#!/bin/bash

aws dynamodb delete-item \
	--endpoint-url http://localhost:8042 \
	--table-name product-info \
  	--key '{"productId": {"S": "999"}}' \
  	--return-values ALL_OLD
