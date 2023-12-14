#!/usr/bin/env bash

# Skrypt uruchamia kontener z bazą

. ./env.sh
set -e

if docker start ${DOCKER_DB_NAME} ; then
	echo "Uruchomienie kontenera z postgresem zakończone sukcesem"
else
	echo "Brak kontenera z postgresem! Aby utworzyć kontener wykonaj skrypt create_database.sh"
fi
