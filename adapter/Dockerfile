FROM --platform=linux/amd64 amazoncorretto:17.0.8-alpine3.18

ARG FROM_JAR=build/libs/adapter.jar

COPY ${FROM_JAR} app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "app.jar"]