#!/bin/bash
set -e
# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson

# Check if BUILD_NUMBER is set, if not lets assume its local
if [ -z ${BUILD_NUMBER+x} ]; then export BUILD="build-local"; else export BUILD="build-${BUILD_NUMBER}"; fi

echo "Starting CI environment"
docker-compose --project-name continous-integration -f docker-compose.ci.yaml up -d

echo "Sleep 8 seconds before running tests on CI environment"
sleep 8

echo "Get into system_tests directory"
cd tests/integration_tests

echo "Executing tests"
# mvn test
