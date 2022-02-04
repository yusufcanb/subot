FROM maven:3.8-jdk-11 as BUILD
WORKDIR /opt/subot-api

COPY . /opt/subot-api

RUN mvn clean package

FROM openjdk:11

RUN mkdir /app

COPY --from=BUILD /opt/subot-api/target/subot-api-*.jar /app/platform.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/platform.jar"]