#!/bin/bash
set -e

echo "Get into system_tests directory"
cd "$(dirname "$0")"

echo "Starting CI environment"
docker-compose up -d 

echo "Sleep 6 seconds before running tests on CI environment"
sleep 6

echo "Executing tests"
mvn test
