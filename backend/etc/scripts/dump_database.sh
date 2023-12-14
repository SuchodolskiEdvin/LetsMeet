#!/usr/bin/env bash

# Skrypt tworzy zrzut bazy

. ./env.sh
set -e

docker exec -ti ${DOCKER_DB_NAME} sh -c "export PGPASSWORD=${DB_NAME} & pg_dump -h localhost -p 5432 -U ${DB_NAME} > /dump.sql"
docker cp ${DOCKER_DB_NAME}:/dump.sql ${DB_PATH}/example_data/

# Wyłączenie constraintów - wydaje się niepotrzebne w tym przypadku - zostaje na wszelki wypadek
# sed -i '1s;^;SET session_replication_role = replica\;\n;' ./sql/example_data/dump.sql

cd ${DB_PATH}/example_data
tar -zcf dump.tgz dump.sql
rm ${DB_PATH}/example_data/dump.sql
