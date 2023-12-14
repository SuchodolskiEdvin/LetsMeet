#!/usr/bin/env bash

# Skrypt kasuje kontener z bazą

. ./env.sh
set -e

if docker rm -f ${DOCKER_DB_NAME}; then
	echo "Kontener z postgresem został usunięty! Utwórz nowy kontener za pomoca skryptu create_database.sh";
else
	echo "Kontener ${DOCKER_DB_NAME} nie jest uruchomiony!";
fi
