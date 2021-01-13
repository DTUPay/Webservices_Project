#!/bin/bash
set -e
# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson

# Check if BUILD environment is set
if [ -z ${BUILD+x} ]; then export BUILD="build-local"; else echo "BUILD is set to '$BUILD'"; fi

# Set service name based on the current directory name
SERVICE_NAME=$(basename $(pwd))

# Compile and build the application
mvn package

# Build Docker image
docker build --build-arg SERVICE_NAME_ARG=$SERVICE_NAME -t $SERVICE_NAME:${BUILD} .
