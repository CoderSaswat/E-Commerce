# Use a minimal Java runtime as the base image
FROM eclipse-temurin:17.0.5_8-jre

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/*.jar /app/app.jar

EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar"]
