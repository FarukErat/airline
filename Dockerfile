# Use the official OpenJDK image from the Docker Hub
# how to integrate the IDE with docker for debugging
FROM openjdk:23-jdk-slim as builder

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the source code into the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Second stage for a smaller image
FROM openjdk:23-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/airline-0.0.1-SNAPSHOT.jar airline.jar

# Expose the port the app runs on
EXPOSE 8000

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "airline.jar"]
