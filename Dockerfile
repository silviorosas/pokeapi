FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/pokeapi-0.0.1-SNAPSHOT.jar /app/demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]
