#!/bin/sh

aws dynamodb get-item \
	--endpoint-url http://localhost:8042 \
	--table-name product-info \
  	--key '{"productId": {"S": "999"}}'
