FROM ghcr.io/appcafe/ibm-semeru-runtimes:open-17-jre-ubi-minimal-amd64

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]