#!/bin/bash
set -e

# Set service name based on the current directory name
SERVICE_NAME=$(basename $(pwd))

# Compile and build the application
mvn package

# Build Docker image
docker build -t $SERVICE_NAME:build-${BUILD_NUMBER} .
© 2021 GitHub, Inc.
