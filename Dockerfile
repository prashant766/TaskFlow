FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew bootJar -x test

CMD ["java", "-jar", "build/libs/TASK_FLOW-0.0.1-SNAPSHOT.jar"]