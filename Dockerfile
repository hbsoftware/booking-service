WORKDIR /booking-service
FROM khipu/openjdk17-alpine
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
