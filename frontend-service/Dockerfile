# Étape 1 : Build Maven avec JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build

# Installation utilitaires (curl pour healthcheck si besoin)
RUN apt-get update && apt-get install -y curl

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY src ./src

# Compiler sans exécuter les tests
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution avec JRE 21
FROM eclipse-temurin:21-jre

# Définir le répertoire de travail dans l'image finale
WORKDIR /app

# Copier le JAR généré depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
