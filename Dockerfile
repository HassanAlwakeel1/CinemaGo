# Use Eclipse Temurin JDK 21 (official OpenJDK builds)
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the fat JAR built by Maven
COPY target/CinemaGo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 2200

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
