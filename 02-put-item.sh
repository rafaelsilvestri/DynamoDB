#!/bin/bash

aws dynamodb put-item \
	--endpoint-url http://localhost:8042 \
	--table-name product-info \
  	--item '{"productId": {"S": "999"}, "email": {"S": "rafaelcechinel@gmail.com"}}'
