#!/bin/sh
awslocal cloudformation create-stack --stack-name dynamodb --template-body file://dynamodb.yml