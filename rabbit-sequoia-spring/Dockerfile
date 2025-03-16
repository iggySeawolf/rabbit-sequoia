#FROM openjdk:21
#COPY . /usr/src/myapp
#WORKDIR /usr/src/myapp
#RUN javac RabbitArnabApplication.java
#CMD ["java", "Main"]



#FROM openjdk:21
#ARG JAR_FILE=./rabbit-sequoia-spring/target/rabbit-arnab-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

# === Stage 1: Build the JAR ===
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy the project files
COPY ./rabbit-sequoia-spring /app

# Build the JAR
RUN mvn clean package -DskipTests

# === Stage 2: Run the Application ===
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/rabbit-arnab-0.0.1-SNAPSHOT.jar app.jar

# Healthcheck to ensure the service is running
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]
