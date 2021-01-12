#!/bin/Bash

echo "Get into system_tests directory"
cd "$(dirname "$0")"

echo "Building and running integration tests!"
cat docker-compose.yaml
docker-compose pull
docker-compose up
