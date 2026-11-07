FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -r -u 10001 appuser && mkdir -p /var/myhealthvault/files && chown -R appuser:appuser /var/myhealthvault
COPY --from=build /workspace/target/myhealthvault-api-1.0.0.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
