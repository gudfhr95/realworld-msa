FROM openjdk:11-jre-slim

EXPOSE 9000

ADD ./build/libs/*jar app.jar

ENTRYPOINT ["java", "-jar", "-DSpring.profiles.active=docker", "/app.jar"]