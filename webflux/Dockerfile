# Stage 1: Build the JAR file using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS stage1

# Set working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Package the application (skip tests to speed up build)
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM openjdk:21

# Set working directory for runtime
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=stage1 /app/target/webflux-0.0.1-SNAPSHOT.jar webflux-0.0.1-SNAPSHOT.jar

# Run the application
ENTRYPOINT ["java", "-jar", "webflux-0.0.1-SNAPSHOT.jar"]
