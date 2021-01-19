#!/bin/bash
set -e
# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson

# Check if BUILD_NUMBER is set, if not lets assume its local
if [ -z ${BUILD_NUMBER+x} ]; then export BUILD="build-local"; else export BUILD="build-${BUILD_NUMBER}"; fi

echo "Starting CI environment"
docker-compose --project-name continous-integration -f docker-compose.ci.yaml up -d

echo "Waiting for the CI environment to be ready"
until $(curl --output /dev/null --silent --fail "http://localhost:18080/management_service/debug"); do
  echo "Service are not ready yet..."
  sleep 5
done

echo "Get into system_tests directory"
cd tests

echo "Executing tests"
mvn test
