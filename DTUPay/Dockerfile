FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src
COPY target/DTUPay-1.0.0-SNAPSHOT-runner.jar /usr/src
CMD java -Xmx64m \
    -jar DTUPay-1.0.0-SNAPSHOT-runner.jar
