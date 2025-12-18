# Multi-stage build for Java application
FROM eclipse-temurin:17-jdk-alpine as builder

# Set working directory
WORKDIR /app

# Copy source code
COPY ["Crop Managment System 1/src", "./src"]
COPY ["Crop Managment System 1/build.xml", "./"]
COPY ["Crop Managment System 1/manifest.mf", "./"]

# Install Ant for building
RUN apk add --no-cache apache-ant

# Build the application
RUN ant compile jar

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install PostgreSQL client
RUN apk add --no-cache postgresql-client

# Set working directory
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/dist/*.jar ./crop-management-system.jar

# Copy PostgreSQL JDBC driver
ADD https://jdbc.postgresql.org/download/postgresql-42.7.1.jar ./lib/postgresql-42.7.1.jar

# Set classpath
ENV CLASSPATH="/app/crop-management-system.jar:/app/lib/postgresql-42.7.1.jar"

# Expose port (if needed for future web interface)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD java -cp $CLASSPATH -Djava.awt.headless=true model.Crop || exit 1

# Run the application
CMD ["java", "-cp", "/app/crop-management-system.jar:/app/lib/postgresql-42.7.1.jar", "view.LoginPage"]