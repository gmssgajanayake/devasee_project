#FROM openjdk:17-jdk-slim
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/users-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
