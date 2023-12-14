#!/bin/bash

. ./env.sh

cd ${FRONTEND_PATH}

# backend generuje swagger.json
wget http://localhost:5900/v2/api-docs -O swagger.json

# frontend na podstawie swagger.json generuje zapytania do api
grunt swagger-vue-codegen

