#!/bin/Bash

echo "Get into system_tests directory"
cd "$(dirname "$0")"

echo "Starting CI environment"
docker-compose up -d 

echo "Sleep 15 seconds before running tests on CI environment"
sleep 15

echo "Executing tests"
mvn test
