# Ejemplo: agenda/Dockerfile
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# Copiar archivos Maven
COPY pom.xml .
COPY src ./src

# Construir el JAR
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

# Etapa final
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]