#!/usr/bin/env bash

# Skrypt pozwalający połączyć się z bazą danych postgresa na dockerze

. ./env.sh
set -e

docker exec -ti ${DOCKER_DB_NAME} sh -c "export PGPASSWORD=${DB_NAME} & psql -h localhost -p 5432 -U ${DB_NAME}"
