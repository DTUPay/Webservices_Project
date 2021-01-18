#!/bin/bash
set -e
# author: Rubatharisan Thirumathyam

echo "Hiya! Welcome to DTUPay!"
./scripts/build_and_test_all.sh
BUILD="build-local" docker-compose --project-name production -f docker-compose.yaml up -d
