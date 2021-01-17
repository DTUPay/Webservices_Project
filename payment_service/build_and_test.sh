#!/bin/bash
set -e
# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson

# Check if BUILD environment is set
if [ -z ${BUILD+x} ]; then export BUILD="build-local"; else echo "BUILD is set to '$BUILD'"; fi

# Set service name based on the current directory name
SERVICE_NAME=$(basename $(pwd))

# Printing out information
echo "# Building service: ${SERVICE_NAME}"

# Build Docker image using BuildKit
#DOCKER_BUILDKIT=1 BUILDKIT_PROGRESS=plain docker build --build-arg SERVICE_NAME_ARG=$SERVICE_NAME -t $SERVICE_NAME:${BUILD} .
