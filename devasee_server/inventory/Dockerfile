FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/inventory-1.0-SNAPSHOT.jar app.jar
#COPY pem/gateway-public.pem /etc/keys/public.pem
ENTRYPOINT ["java","-jar","app.jar"]
