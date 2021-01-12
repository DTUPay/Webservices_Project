# Author: Rubatharisan Thirumathyam
#!/bin/bash
set -e

# Check if BUILD environment is set
if [ -z ${BUILD+x} ]; then export BUILD="build-local"; else echo "BUILD is set to '$BUILD'"; fi

# Set service name based on the current directory name
SERVICE_NAME=$(basename $(pwd))

# Compile and build the application
mvn package

# Build Docker image
docker build -t $SERVICE_NAME:${BUILD} .
