# Build stage
FROM maven:3.9.10-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the jar file
COPY --from=build /app/target/demo-shaded.jar app.jar

EXPOSE 8081

# Use exec form and add JVM optimization flags
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]