# Etapa 1: Construcción (Usamos Maven para compilar el código)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compilamos el proyecto y saltamos los tests para que sea más rápido
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Usamos una versión ligera de Java para correr la app)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Exponemos el puerto 8080
EXPOSE 8080
# Comando para encender Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]