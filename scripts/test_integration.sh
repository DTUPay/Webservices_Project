#!/bin/bash
set -e

# Check if BUILD is set, if not lets assume its local
if [ -z ${BUILD+x} ]; then export BUILD="build-local"; else echo "BUILD is set to '$BUILD'"; fi

echo "Get into system_tests directory"
cd "$(dirname "$0")"

echo "Starting CI environment"
docker-compose -f docker-compose.ci.yaml up -d 

echo "Sleep 6 seconds before running tests on CI environment"
sleep 6

echo "Executing tests"
mvn test
