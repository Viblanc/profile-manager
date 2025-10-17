FROM maven:3.9-eclipse-temurin-17 AS build
COPY target/profile-manager-0.0.1-SNAPSHOT.jar /app/profile-manager.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "profile-manager.jar"]
