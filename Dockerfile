FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/budget.jar budget.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "budget.jar"]