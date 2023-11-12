FROM openjdk:8-jdk-alpine


RUN apt-get update && \
    apt-get install -y curl && \
    curl -o Project.jar -L "http://192.168.33.10:8081/repository/maven-releases/tn/esprit/DevOps_Project/1.0/DevOps_Project-1.0.jar" && \
    apt-get remove -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "Project.jar"]