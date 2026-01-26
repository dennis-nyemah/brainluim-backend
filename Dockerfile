# Stage 1: Build stage
# We use the specific Maven 3.9.11 image to match your local environment
FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the pom.xml first to fetch dependencies (caching layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Use a non-root user for better security on Render
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"]