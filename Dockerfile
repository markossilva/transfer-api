FROM amazoncorretto:11
ARG JAR_FILE
COPY /target/transfer-api.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]