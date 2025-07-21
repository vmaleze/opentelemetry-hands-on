FROM docker.io/alpine/java:21-jre

RUN mkdir -p /app/bin

COPY target/*.jar /app/bin/app.jar

CMD ["java", "-jar", "/app/bin/app.jar"]
