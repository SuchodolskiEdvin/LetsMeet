#!/usr/bin/env bash

# Skrypt tworzy kontener z bazą

. ./env.sh
set -e

if docker create --name ${DOCKER_DB_NAME} -p 5500:5432 -e POSTGRES_DB=${DB_NAME} -e POSTGRES_USER=${DB_NAME} -e POSTGRES_PASSWORD=${DB_NAME} postgres:14.3 ; then
	echo "Utworzony został kontener z postgresem! Aby go uruchomić wykonaj skrypt start_database.sh"
else
	echo "Kontener o nazwie ${DOCKER_DB_NAME} już istnieje! Usuń go za pomocą skryptu delete_database.sh"
fi
