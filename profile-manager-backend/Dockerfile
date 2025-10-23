FROM maven:3.9-eclipse-temurin-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean install -Dmaven.test.skip=true

FROM eclipse-temurin:17
COPY --from=build /app/target/profile-manager-0.0.1-SNAPSHOT.jar /app/profile-manager.jar
WORKDIR /app
CMD ["java", "-jar", "profile-manager.jar"]
