FROM openjdk:11-jre-slim

EXPOSE 9001

ADD ./build/libs/*jar app.jar

ENTRYPOINT ["java", "-jar", "-DSpring.profiles.active=docker", "/app.jar"]