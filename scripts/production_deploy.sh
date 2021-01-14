#!/bin/bash
set -e
# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson

# Find all build_and_test.sh files in subdirectories, making it possible to extract services
build_and_tests=$(find . -mindepth 2 -maxdepth 2 -name build_and_test.sh)

# For each build_and_test.sh file, do the following
for build_and_test in $build_and_tests
do
  # Extract parent folder
  service_name=$(basename "$(dirname "$build_and_test")")

  # Re-tagging service to stable
  docker tag "${service_name}:build-${BUILD_NUMBER}" "${service_name}:stable"
done

# Deploy to stable production
echo "Deploying and starting production environment"
BUILD="stable" docker-compose --project-name production -f docker-compose.yaml up -d
