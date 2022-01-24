FROM maven:3.8.3-openjdk-17-slim AS build
COPY src /home/application/src
COPY pom.xml /home/application
USER root
RUN --mount=type=cache,target=/.m2 mvn -f /home/application/pom.xml clean package

FROM openjdk:17-oracle
COPY --from=build /home/application/target/application.jar /usr/local/lib/application.jar
#EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/usr/local/lib/application.jar"]
