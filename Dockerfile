FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /workspace/app

COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim
VOLUME /tmp

COPY --from=build /workspace/app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
