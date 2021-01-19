#!/bin/bash
set -e
# author: Rubatharisan Thirumathyam

echo "Hiya! Welcome to DTUPay!"
./scripts/build_and_test_all.sh
BUILD="build-local" docker-compose --project-name production -f docker-compose.yaml up -d

until $(curl --output /dev/null --silent --fail "http://localhost:8080/management_service/debug"); do
  echo "Service are not ready yet..."
  sleep 5
done

echo "All services are ready! Please run: make test"
