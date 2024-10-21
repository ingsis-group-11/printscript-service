# Etapa de compilación
FROM gradle:8.10.1-jdk21 AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /home/gradle/src

# Copiar los archivos del proyecto al contenedor
COPY . .

# Set environment variables using Docker secrets and ejecutar el build
RUN --mount=type=secret,id=gpr_user \
    --mount=type=secret,id=gpr_token \
    sh -c 'export USERNAME=$(cat /run/secrets/gpr_user) && export TOKEN=$(cat /run/secrets/gpr_token) && echo "USERNAME: $USERNAME, TOKEN: $TOKEN" && gradle assemble --no-daemon'

# Etapa final
FROM openjdk:21-jdk-slim
EXPOSE 8080

# Crear directorio para la app
RUN mkdir /app

# Copiar el archivo JAR generado desde la etapa de build
COPY --from=build /home/gradle/src/build/libs/*.jar /app/printscript-service.jar

# Configurar el comando de entrada para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/app/printscript-service.jar"]
