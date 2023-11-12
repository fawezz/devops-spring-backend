FROM openjdk:8-jdk-alpine

RUN apk add --no-cache curl \
    && curl -o Project.jar -L "http://192.168.33.10:8081/repository/maven-releases/tn/esprit/DevOps_Project/1.0/DevOps_Project-1.0.jar" \
    && apk del curl && ls
CMD ls

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "Project.jar"]
