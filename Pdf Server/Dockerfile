FROM maven:3.8.3-openjdk-17-slim AS build
COPY src /home/application/src
COPY pom.xml /home/application
USER root
RUN --mount=type=cache,target=/.m2 mvn -f /home/application/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/application/target/application.jar /usr/local/lib/application.jar
#EXPOSE 80
ENTRYPOINT ["java", "-jar", "/usr/local/lib/application.jar"]
