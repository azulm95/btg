# Etapa 1: build
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar pom y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir el JAR
RUN mvn clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Variables de entorno opcionales (puedes pasar otras desde docker run)
ENV JAVA_OPTS=""

# Comando para ejecutar la app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
