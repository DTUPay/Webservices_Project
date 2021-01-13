# @author: Rubatharisan Thirumathyam & Mikkel Rosenfeldt Anderson
FROM adoptopenjdk:11-jre-hotspot
ARG SERVICE_NAME_ARG=application_service
ENV SERVICE_NAME=$SERVICE_NAME_ARG
WORKDIR /app
COPY target/lib /app/lib
COPY target/${SERVICE_NAME}-1.0-SNAPSHOT-runner.jar .
ENTRYPOINT java -Xmx64m -jar $SERVICE_NAME-1.0-SNAPSHOT-runner.jar
