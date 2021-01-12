# Author: Rubatharisan Thirumathyam
#!/bin/bash
set -e

# Check if BUILD_NUMBER is set, if not lets assume its local
if [ -z ${BUILD_NUMBER+x} ]; then export BUILD_NUMBER="local"; else echo "BUILD_NUMBER is set to '$BUILD_NUMBER'"; fi

# Set service name based on the current directory name
SERVICE_NAME=$(basename $(pwd))

# Compile and build the application
mvn package

# Build Docker image
# docker build -t $SERVICE_NAME:build-${BUILD_NUMBER} .
