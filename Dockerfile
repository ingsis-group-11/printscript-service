FROM gradle:8.10.1-jdk21 AS build

# Set environment variables using Docker secrets
RUN --mount=type=secret,id=gpr_user \
    --mount=type=secret,id=gpr_token \
    sh -c 'export USERNAME=$(cat /run/secrets/gpr_user) && export TOKEN=$(cat /run/secrets/gpr_token) && gradle assemble --no-daemon'

FROM openjdk:21-jdk-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/printscript-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/app/printscript-service.jar"]
