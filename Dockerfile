# Estágio 1: Build da aplicação
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final (Execução)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia apenas o JAR gerado no primeiro estágio
COPY --from=build /app/target/*.jar app.jar

# Define as variáveis de ambiente necessárias para o Actuator/Logging
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]