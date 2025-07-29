FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

RUN apt-get update && \
    apt-get install -y iputils-ping dnsutils netcat && \
    rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/budget.jar budget.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "budget.jar"]
