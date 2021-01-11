#!/bin/bash

# Compile and build the application
./mvnw package

# Test application
./mvnw test

# Build Docker image
docker build -t DTUPay:build-${BUILD_NUMBER} .
