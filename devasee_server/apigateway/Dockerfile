FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/apigateway-1.0-SNAPSHOT.jar app.jar
#COPY pem /etc/keys
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]