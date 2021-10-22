FROM openjdk:11
RUN mkdir -p /app
WORKDIR /app
COPY ./build/libs/backend-1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]