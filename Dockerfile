FROM openjdk:17-jdk
LABEL maintainer="mhy5413@gmail.com"
ARG JAR_FILE=./build/libs/bayclip-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]