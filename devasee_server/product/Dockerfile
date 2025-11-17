#FROM openjdk:17-jdk-slim
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/product-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
