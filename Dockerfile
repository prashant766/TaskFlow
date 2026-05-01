# Use Java 21
FROM eclipse-temurin:21-jdk

# Set working dir
WORKDIR /app

# Copy project
COPY . .

# Build app
RUN chmod +x gradlew
RUN ./gradlew build -x test

# Run app
CMD ["java", "-jar", "build/libs/TaskFlow-0.0.1-SNAPSHOT.jar"]