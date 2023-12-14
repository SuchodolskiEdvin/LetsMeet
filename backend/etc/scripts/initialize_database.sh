#!/usr/bin/env bash

# Skrypt inicjalizuje bazę danymi ze zrzutu

. ./env.sh
set -e

tar -C ${DB_PATH}/example_data/ -zxf ${DB_PATH}/example_data/dump.tgz
docker cp ${DB_PATH}/example_data/dump.sql ${DOCKER_DB_NAME}:/dump.sql
docker exec -ti ${DOCKER_DB_NAME} sh -c "export PGPASSWORD=${DB_NAME} & psql -h localhost -d ${DB_NAME} -p 5432 -U ${DB_NAME} -f dump.sql"
