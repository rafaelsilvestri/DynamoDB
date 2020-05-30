#!/bin/bash

aws dynamodb update-item \
	--endpoint-url http://localhost:8042 \
	--table-name person-info \
  	--key '{"personId": {"S": "999"}}' \
  	--update-expression 'SET #isEligibleForPromotion = :eligibility' \
  	--expression-attribute-names '{"#isEligibleForPromotion": "isEligibleForPromotion", "#dateOfBirth": "dateOfBirth"}' \
  	--expression-attribute-values '{":eligibility": {"BOOL": true}, ":dateFrom": {"S": "1980-01-01"}}' \
  	--condition-expression "#dateOfBirth > :dateFrom" \
  	--return-values ALL_NEW

##############################################################3
# return-values is optional and can assume these values:
# NONE: if we simply do not include return-values, it will be defaulted to NONE. It means no values will be returned.
# ALL_OLD: it will return all the attributes or columns of the item as they were before the update.
# UPDATED_OLD: it will return only the attributes that were updated as they were before the update.
# ALL_NEW: it will return all the attributes or columns of the item as they are after the update.
# UPDATED_NEW: it will return only the attributes of the item as they are after the update.
