FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY target/lib /app/lib
COPY target/DTUPay-1.0.0-SNAPSHOT-runner.jar .
CMD java -Xmx64m -jar DTUPay-1.0.0-SNAPSHOT-runner.jar
