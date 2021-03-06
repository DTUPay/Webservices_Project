# syntax = docker/dockerfile:1.2
# @author: Rubatharisan Thirumathyam

# Multi-stage build: build phase and run phase
# Lets base our development image on a Maven + Java JDK image
FROM maven:3.6.3-jdk-11-slim AS buildstage

# Setting the service name argument as a environment variable
ENV SERVICE_NAME=customer_service

# Let build know, that it is CI environment.
ENV CONTINUOUS_INTEGRATION=true

# Lets copy our source code to the image
COPY . /app-code

# Switch working directory
WORKDIR /app-code

# Build the code
RUN --mount=type=cache,target=/root/.m2 mvn package

# Verify the package
RUN --mount=type=cache,target=/root/.m2 mvn verify

# Artifacts folder with final artifacts
RUN mkdir artifacts \
    && mv target/customer_service-1.0-SNAPSHOT-runner.jar target/lib/ artifacts/

# Test stage -

# Lets create our final image with the java artifacts
FROM adoptopenjdk:11-jre

# Setting a default argument of service name if none is giving as a build-arg
ARG SERVICE_NAME_ARG=application_service

# Setting the service name argument as a environment variable
ENV SERVICE_NAME=$SERVICE_NAME_ARG

# Lets indicate our exposed port
EXPOSE 8080

# Copy artifacts from our builder image to our runtime image
COPY --from=buildstage /app-code/artifacts /app

# Switch working directory
WORKDIR /app

# Entrypoint which starts our runner
ENTRYPOINT java -Xmx64m -jar customer_service-1.0-SNAPSHOT-runner.jar
