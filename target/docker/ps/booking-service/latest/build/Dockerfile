FROM quay.io/jkube/jkube-java:0.0.19
ENV JAVA_APP_DIR=/deployments
LABEL org.label-schema.description="Simple Case study app project in Spring Boot" org.label-schema.version=0.0.1-SNAPSHOT org.label-schema.schema-version=1.0 org.label-schema.build-date=2023-12-13T20:32:16.368757900 org.label-schema.name=booking-service org.label-schema.vcs-ref=c70644dc437382835301cdb2578720362ca42ec7 org.label-schema.vcs-url=https://github.com/hbutalia/Book4Fun.git
EXPOSE 8081 8778 9779
COPY /jkube-generated-layer-original/deployments /deployments/
