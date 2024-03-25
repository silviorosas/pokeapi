FROM  openjdk:17-jdk-slim
COPY target/pokeapi-0.0.1-SNAPSHOT.jar demo.jar
ENTRYPOINT ["java","-jar","demo.jar"]