#!/bin/bash

aws dynamodb put-item \
	--endpoint-url http://localhost:8042 \
	--table-name person-info \
  	--item '{"personId": {"S": "999"}, "email": {"S": "rafaelcechinel@gmail.com"}}'
