#!/bin/bash

aws dynamodb put-item \
	--endpoint-url http://localhost:8042 \
	--table-name product-info \
  	--item '{"productId": {"S": "999"}, "email": {"S": "rafaelcechinel@gmail.com"}}' \
  	--condition-expression "attribute_not_exists(productId)"

### and error is expected since the put-item-product.sh allready created a product with id 999
### The condition-expression argument can also be used for update-item and delete-item operations.
