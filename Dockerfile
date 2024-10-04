# Use the Gradle image to build the project
FROM gradle:8.10.1-jdk21 AS build

# Set build arguments for GitHub Packages credentials
ARG GPR_USER
ARG GPR_TOKEN

# Set environment variables for Gradle
ENV USERNAME=$GPR_USER
ENV TOKEN=$DOCKER_TOKEN

# Copy the project files to the build image
COPY . /home/gradle/src

# Set the working directory
WORKDIR /home/gradle/src

# Build the project
RUN gradle assemble --no-daemon

# Use the OpenJDK image to run the application
FROM openjdk:21-jdk-slim

# Expose the application port
EXPOSE 8080

# Create an application directory
RUN mkdir /app

# Copy the built application from the build image
COPY --from=build /home/gradle/src/build/libs/*.jar /app/printscript-service.jar

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/app/printscript-service.jar"]