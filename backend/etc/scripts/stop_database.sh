#!/usr/bin/env bash

# Skrypt zatrymuje kontener z bazą

. ./env.sh
set -e

if docker stop ${DOCKER_DB_NAME}; then
	echo "Kontener z postgresem został zatrzymany";
else
	echo "Kontener ${DOCKER_DB_NAME} nie jest uruchomiony lub nie został utworzony!";
fi
