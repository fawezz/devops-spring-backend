FROM openjdk:8-jdk-alpine

RUN apk add --no-cache curl \
    && curl -o Project.jar -L "http://192.168.33.10:8181/repository/maven-releases/tn/esprit/rh/achat/1.0/achat-1.0.jar" \
    && apk del curl

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "Project.jar"]