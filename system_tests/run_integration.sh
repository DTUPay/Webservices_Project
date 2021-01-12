#!/bin/Bash

echo "Get into system_tests directory"
cd "$(dirname "$0")"

echo "Starting CI environment"
docker-compose up -d 

echo "Sleep 6 seconds before running tests on CI environment"
sleep 6

echo "Executing tests"
mvn test

echo "Cleanup CI environment"
docker-compose down --rmi all
