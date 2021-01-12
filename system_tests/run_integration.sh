#!/bin/bash
set -e

# Check if BUILD_NUMBER is set, if not lets assume its local
if [ -z ${BUILD_NUMBER+x} ]; then export BUILD_NUMBER="local"; else echo "BUILD_NUMBER is set to '$BUILD_NUMBER'"; fi

echo "Get into system_tests directory"
cd "$(dirname "$0")"

echo "Starting CI environment"
docker-compose up -d 

echo "Sleep 6 seconds before running tests on CI environment"
sleep 6

echo "Executing tests"
mvn test
