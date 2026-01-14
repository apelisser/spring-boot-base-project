# Stage 1 - Build
FROM eclipse-temurin:25-jdk-jammy AS build

WORKDIR /build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw --batch-mode --quiet dependency:go-offline

COPY src src
RUN ./mvnw --batch-mode --quiet package -DskipTests

# Stage 2 - Runtime
FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

RUN useradd -r -u 1001 spring

USER spring

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseContainerSupport", \
  "-jar", \
  "app.jar"]
