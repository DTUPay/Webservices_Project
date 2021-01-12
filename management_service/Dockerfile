FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY target/lib /app/lib
COPY target/management_service-1.0-SNAPSHOT-runner.jar .
CMD java -Xmx64m -jar management_service-1.0-SNAPSHOT-runner.jar
