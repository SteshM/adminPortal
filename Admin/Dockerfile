FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/Admin-0.0.1-SNAPSHOT.jar admin.jar
ENTRYPOINT [ "java", "-jar", "admin.jar"]