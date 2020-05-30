#!/bin/sh

aws dynamodb get-item \
	--endpoint-url http://localhost:8042 \
	--table-name person-info \
  	--key '{"personId": {"S": "999"}}'
